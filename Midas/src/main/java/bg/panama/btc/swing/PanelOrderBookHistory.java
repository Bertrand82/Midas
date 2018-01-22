package bg.panama.btc.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import bg.panama.btc.model.OrderBook;
import bg.panama.btc.model.OrderBookItem;

public class PanelOrderBookHistory {

	JTabbedPane tabbedPane = new JTabbedPane();

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
    private int i=0;
    List<OrderBookItem> listTotaleBookOrderItem= new ArrayList<>();
    List<PanelOrderBook> listPAnelOrderBook = new ArrayList<>();
    
	public void addOrderBook(OrderBook book) {
		PanelOrderBook panelOrderBook = new PanelOrderBook(book, listTotaleBookOrderItem);
		
		this.tabbedPane.addTab(""+i++,panelOrderBook);
		panelOrderBook.setVisible(true);
		for(PanelOrderBook p : listPAnelOrderBook){
			p.update(listTotaleBookOrderItem);
		}
	}
	
	
}
