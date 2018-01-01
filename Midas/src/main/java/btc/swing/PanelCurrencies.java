package btc.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Hashtable;

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
	String[] columnNames = { "Symbol", "possesion (dollar)", "%", "% filtre","Eligible" };

	AbstractTableModel tableModel = new AbstractTableModel() {
		int nSelect = 4;
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
			SessionCurrency t = (SessionCurrency) session.getListOrder_byDailyChangePerCent().get(rowIndex);
			Balance balance = session.getBalance(t.getShortName());
			if (columnIndex == 0) {
				return t.getShortName();
			} else if (columnIndex == 1) {
				if (balance == null) {
					return "-";
				}else if (balance.getAvailableInDollar() <=0.01){
					return "0";
				} else {
					return df.format(balance.getAvailableInDollar()) + " $";
				}

			} else if (columnIndex == 2) {
				double taux =t.getTicker_Z_1().getDaylyChangePerCent();
				return df2.format( taux)+ " %";
			} else if (columnIndex == 3) {
				double taux = t.getDaylyChangePerCent();
				return df2.format( taux) + " %";
			}else if(columnIndex == nSelect){
				return t.isEligible();
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
			}
			return Object.class;
		}

	};
	private JTable table;

	public PanelCurrencies(SessionCurrencies session) {
		super(new BorderLayout());
		this.session = session;
		table = new JTable(tableModel);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.SOUTH);
		this.add(labelTitre, BorderLayout.CENTER);
		this.update(session);

	}

	private static DecimalFormat df = new DecimalFormat("0,000,000.00");
	private static DecimalFormat df2 = new DecimalFormat(".000");

	public void update(SessionCurrencies session) {
		this.session = session;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				table.updateUI();
				Date date = new Date();
				labelTitre.setText("Total "+df.format(session.getBalancesCurrent().getTotalDollar())+" $ "+date);
			}
		});
	}

}
