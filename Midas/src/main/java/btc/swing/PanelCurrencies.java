package btc.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.text.DecimalFormat;
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
				} else {
					return balance.getAvailableInDollar() + " $";
				}

			} else if (columnIndex == 2) {
				return t.getTicker_Z_1().getDaylyChangePerCent() + " %";
			} else if (columnIndex == 3) {
				return t.getDaylyChangePerCent() + " %";
			}else if(columnIndex == nSelect){
				return t.isEligible();
			}
			return "";
		}
		
		public void setValueAt(Object value, int row, int col) {
			
			if (col == nSelect){
				SessionCurrency t = (SessionCurrency) session.getListOrder_byDailyChangePerCent().get(row);
				t.setEligible((Boolean) value);
			}
			
		}

		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == nSelect){
				return Boolean.class;
			}
			return Object.class;
		}

	};
	JTable table;

	public PanelCurrencies(SessionCurrencies session) {
		super(new BorderLayout());
		this.session = session;
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.SOUTH);
		this.add(labelTitre, BorderLayout.CENTER);

	}

	DecimalFormat df = new DecimalFormat("0000000.00");

	public void update(SessionCurrencies session) {
		this.session = session;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				table.updateUI();
			}
		});
	}

}
