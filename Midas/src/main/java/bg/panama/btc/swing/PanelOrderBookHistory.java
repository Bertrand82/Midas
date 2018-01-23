package bg.panama.btc.swing;

import java.util.ArrayList;
import java.util.Date;
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
    List<OrderBookItem> listTotaleBookOrderItemMinMax= new ArrayList<>();
    List<PanelOrderBook> listPAnelOrderBook_ = new ArrayList<>();
    
	public void addOrderBook(OrderBook book) {
		PanelOrderBook panelOrderBook = new PanelOrderBook(new Date(),book);
		System.out.println("addOrderBook book :"+book);
		this.listTotaleBookOrderItemMinMax.add(book.getOrderBookItemMax());
		this.listTotaleBookOrderItemMinMax.add(book.getOrderBookItemMin());
		this.listTotaleBookOrderItemMinMax.add(book.getOrderBookItemAmmountMax());
		this.tabbedPane.addTab(""+i++,panelOrderBook);
		panelOrderBook.setVisible(true);
		this.listPAnelOrderBook_.add(panelOrderBook);
		processMinMax();
		for(PanelOrderBook p : listPAnelOrderBook_){
			p.update(maxPrice,minPrice,ammountMax);
		}
		tabbedPane.setSelectedIndex(i-1);
	}
	double ammountMax = 0;
	double maxPrice =0;
	double minPrice =1000000000000000000d;
	private void processMinMax() {
		maxPrice = listTotaleBookOrderItemMinMax.get(0).getPrice();
		minPrice = maxPrice;
		ammountMax=0;
		for(OrderBookItem obi : listTotaleBookOrderItemMinMax){
			if (obi.getPrice() > maxPrice)
				maxPrice = obi.getPrice();
			  
			if (obi.getPrice() < minPrice)
				minPrice = obi.getPrice();
			    
			if (obi.getAmount() > ammountMax)
				ammountMax = obi.getAmount();

		}
	}
	
	
}
