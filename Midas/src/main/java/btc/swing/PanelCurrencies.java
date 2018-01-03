package btc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import btc.model.Balance;
import btc.model.Balances;
import btc.model.v2.ITicker;
import btc.trading.first.SessionCurrencies;
import btc.trading.first.SessionCurrency;

public class PanelCurrencies extends JPanel {

	private static final long serialVersionUID = 1L;
	SessionCurrencies session;
	private JLabel labelTitre = new JLabel("Total ");
	String[] columnNames = { "Symbol", "possesion (dollar)", "% Day", "% Hour f", "% Hour instant","Eligible" ,""};
    private Hashtable<String, PanelCanvas> hCanvas = new Hashtable<>();
	AbstractTableModel tableModel = new AbstractTableModel() {
		int nSelect = 5;
		private static final long serialVersionUID = 1L;

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public int getRowCount() {
			return session.getListOrder_byDailyChangePerCent().size();
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
			SessionCurrency sessionCurrency = (SessionCurrency) session.getListOrder_byDailyChangePerCent().get(rowIndex);
			Balance balance = session.getBalance(sessionCurrency.getShortName());
			if (columnIndex == 0) {
				return sessionCurrency.getShortName();
			} else if (columnIndex == 1) {
				if (balance == null) {
					return "-";
				}else if (balance.getAvailableInDollar() <=0.01){
					return "0";
				} else {
					return df.format(balance.getAvailableInDollar()) + " $";
				}

			} else if (columnIndex == 2) {
				double taux =sessionCurrency.getTicker_Z_1().getDaylyChangePerCent();				
				return df2.format( taux)+ " %";
			} else if (columnIndex == 3) {
				double taux = sessionCurrency.getHourlyChangePerCentByDay();
			
				return df2.format( taux) + " %";
			} else if (columnIndex == 4) {
				double taux = sessionCurrency.getHourlyChangePerCentByDayInstant();
				
				return df2.format(taux) + " %";
			}else if(columnIndex == nSelect){
				return sessionCurrency.isEligible();
			}else if(columnIndex == 6){
				String key =  sessionCurrency.getShortName();
				return hCanvas.get(key).getImageIcon();
			}
			return "";
		}
		
		public void setValueAt(Object value, int row, int col) {
			
			if (col == nSelect){
				SessionCurrency t = (SessionCurrency) session.getListOrder_byDailyChangePerCent().get(row);
				Boolean b = (Boolean) value;
				if (b != t.isEligible()){
					t.setEligible((Boolean) value);
					session.saveConfiguration();
				}
			} 
			
		}

		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == nSelect){
				return Boolean.class;
			}else if(columnIndex == 6){
				return ImageIcon.class;
			}
			return Object.class;
		}

	};
	private JTable table;

	public PanelCurrencies(SessionCurrencies session) {
		
		super(new BorderLayout());
		
		this.session = session;
		initCanvas();
		table = new JTable(tableModel);
		table.setRowHeight(60);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.setAutoCreateRowSorter(true);
		
		JScrollPane scrollPane = new JScrollPane(table);
		Dimension dim = new Dimension(1200, 400);
		scrollPane.setPreferredSize(dim);
		scrollPane.setMinimumSize(dim);
		this.add(scrollPane, BorderLayout.SOUTH);
		this.add(labelTitre, BorderLayout.CENTER);
		this.update(session);

	}
	
	void initCanvas(){
		for(SessionCurrency sc : this.session.getListOrder_byHourlyChangePerCent()){
			
			
			int w=199;
			int h =50;
			BufferedImage bf =  new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					String currency = sc.getShortName();
					PanelCanvas pc = new PanelCanvas(currency);
					this.hCanvas.put(currency, pc);
		}
	}

	private static DecimalFormat df = new DecimalFormat("0,000,000.00");
	private static DecimalFormat df2 = new DecimalFormat("0.000;-.000");
	
	public void update(SessionCurrencies session) {
		this.session = session;
		
		Runnable runable = new Runnable() {
			public void run() {
				for(SessionCurrency sc : session.getlSessionCurrency()){
					String key = sc.getShortName();
					PanelCanvas pc = hCanvas.get(key);
					Color color;
					String label = key;
					if (sc.getHourlyChangePerCentByDayInstant() == SessionCurrency.D_default){
						color = Color.BLUE;
						label="Initializing "+key;
					}else if (sc.getHourlyChangePerCentByDayInstant()>0){
						color = Color.GREEN;
					}else {
						color = Color.RED;
					}
					pc.update(label,color);
				}
				table.updateUI();
				Date date = new Date();
				labelTitre.setText("Total "+df.format(session.getBalancesCurrent().getTotalDollar())+" $ "+date);
				
			}
		};
		SwingUtilities.invokeLater(runable);
	}

}
