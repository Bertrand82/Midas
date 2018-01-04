package btc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import btc.model.Balance;
import btc.model.Balances;
import btc.model.v2.ITicker;
import btc.trading.first.SessionCurrencies;
import btc.trading.first.SessionCurrency;

public class PanelCurrencies extends JPanel {
	private static final DecimalFormat df = new DecimalFormat("0,000,000.00");
	private static final DecimalFormat df2 = new DecimalFormat("0.000;-.000");
	private static final DecimalFormat df3 = new DecimalFormat("#######0.000;");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");


	private static final long serialVersionUID = 1L;
	SessionCurrencies session;
	private JLabel labelTitre = new JLabel("Total ");
	String[] columnNames = { "Symbol","montant" ,"= dollar", "% Day", "% Hour f", "% Hour instant","Eligible" ,""};
    private Hashtable<String, PanelCanvas> hCanvas = new Hashtable<>();
    private static final int nSelect = 6;
	AbstractTableModel tableModel = new AbstractTableModel() {
		private List<SessionCurrency> getList() {
		  return session.getListOrder_byHourlyChangePerCentByDay();
		}
		private static final long serialVersionUID = 1L;

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public int getRowCount() {
			return getList().size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		public boolean isCellEditable(int row, int col) {
			if(col == nSelect){
				return true;
			}
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			SessionCurrency sessionCurrency = (SessionCurrency) getList().get(rowIndex);
			Balance balance = session.getBalance(sessionCurrency.getShortName());
			
			if (columnIndex == 0) {
				return sessionCurrency.getShortName();
			} else if (columnIndex == 1) {
				if (balance== null){
					return " - ";
				}else {
					double amount = balance.getAmount();
					return df3.format(amount);
					
				}
			} else if (columnIndex == 2) {
					if (balance == null) {
					return "-";
				}else if (balance.getAvailableInDollar() <=0.01){
					return "0";
				} else {
					return df.format(balance.getAvailableInDollar()) + " $";
				}

			} else if (columnIndex == 3) {
				double taux =sessionCurrency.getTicker_Z_1().getDaylyChangePerCent();				
				return taux;
			} else if (columnIndex == 4) {
				if(sessionCurrency.isInitializing()){
					return 0;
				}else {
					double taux = sessionCurrency.getHourlyChangePerCentByDay();			
					return  taux;
				}
			} else if (columnIndex == 5) {
				if(sessionCurrency.isInitializing()){
					return 0;
				}else {
					double taux = sessionCurrency.getHourlyChangePerCentByDayInstant();
					return taux;
				}
			}else if(columnIndex == nSelect){
				return sessionCurrency.isEligible();
			}else if(columnIndex == (nSelect+1)){
				String key =  sessionCurrency.getShortName();
				return hCanvas.get(key).getImageIcon();
			}
			return "";
		}
		
		public void setValueAt(Object value, int row, int col) {
			
			if (col == nSelect){
				SessionCurrency t = (SessionCurrency) getList().get(row);
				Boolean b = (Boolean) value;
				if (b != t.isEligible()){
					t.setEligible((Boolean) value);
					session.saveConfiguration();
				}
			} 			
		}

		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex){
				case 3:
				case 4:
				case 5 :
					return Double.class;
				case nSelect:
					return Boolean.class;
				case nSelect+1:
					return ImageIcon.class;
			}
			
			return Object.class;
		}

	};
	
	 public class DoubleTableCellRenderer extends DefaultTableCellRenderer {


		private static final long serialVersionUID = 1L;

		public DoubleTableCellRenderer() {
             setHorizontalAlignment(JLabel.RIGHT);
         }

         @Override
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
             if (value instanceof Number) {
                 value = df2.format(value);
             }
             return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }

     }
	private JTable table;

	public PanelCurrencies(SessionCurrencies session) {
		
		super(new BorderLayout());
		
		this.session = session;
		initCanvas();
		table = new JTable(tableModel);
		table.getColumnModel().getColumn(3).setCellRenderer(new DoubleTableCellRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new DoubleTableCellRenderer());
		table.getColumnModel().getColumn(5).setCellRenderer(new DoubleTableCellRenderer());
		table.setRowHeight(60);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(nSelect).setPreferredWidth(40);
		table.getColumnModel().getColumn(nSelect).setMaxWidth(40);
		
		table.setAutoCreateRowSorter(true);
		
		JScrollPane scrollPane = new JScrollPane(table);
		Dimension dim = new Dimension(1200, 400);
		scrollPane.setPreferredSize(dim);
		scrollPane.setMinimumSize(dim);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(labelTitre, BorderLayout.NORTH);
		this.update(session);

	}
	
	void initCanvas(){
		try {
			for(SessionCurrency sc : this.session.getListOrder_byHourlyChangePerCentByDay()){	
						String currency = sc.getShortName();
						PanelCanvas pc = new PanelCanvas(currency);
						this.hCanvas.put(currency, pc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 	public void update(SessionCurrencies session) {
		this.session = session;
		
		Runnable runable = new Runnable() {
			public void run() {
				for(SessionCurrency sc : session.getlSessionCurrency()){
					String key = sc.getShortName();
					PanelCanvas pc = hCanvas.get(key);
					Color color;
					String label = key;
					if (sc.getHourlyChangePerCentByDay() == SessionCurrency.D_default){
						color = Color.BLUE;
						label="Initializing "+key;
					}else if (sc.getHourlyChangePerCentByDay()>0){
						color = Color.GREEN;
					}else {
						color = Color.RED;
					}
					pc.update(label,color);
				}
				table.updateUI();
				Date date = new Date();
				ITicker best = session.getBestEligible();
				String bestEligible ;
				if(best== null){
					bestEligible=" - ";
				}else {
					bestEligible= best.getShortName();
				}
				long duree =System.currentTimeMillis() -  session.getTimeStart().getTime(); 
				String dureeStr = String.format("%02d h  %02d mn", 
					    TimeUnit.MILLISECONDS.toHours(duree),
					    TimeUnit.MILLISECONDS.toMinutes(duree) 	);
				labelTitre.setText("Total :"+df.format(session.getBalancesCurrent().getTotalDollar())+" $ | numero : "+session.getNumero()+" | duree "+dureeStr+" | "+sdf.format(date)+" | best :"+bestEligible);
				
			}
		};
		SwingUtilities.invokeLater(runable);
	}

}
