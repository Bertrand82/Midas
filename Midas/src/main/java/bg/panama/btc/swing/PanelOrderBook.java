package bg.panama.btc.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import bg.panama.btc.model.OrderBook;
import bg.panama.btc.model.OrderBookItem;

public class PanelOrderBook extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final DateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
	int w = 500;
	int h = 500;
	Dimension dim = new Dimension(w, h);
	OrderBook book ;
	List<Item> list = new ArrayList<>();
	Date date = new Date();
	String dateStr ="";
	Font myFont = new Font ("Courier New", Font.BOLD, 25);
	double ammountTotalAsk=0;
	double ammountTotalBids =0;
	int ammountTotalAskPercent = 0;
	int ammountToyalBidPercent =0;
	String label="";
	public PanelOrderBook(Date date, OrderBook book, List<OrderBookItem> l) {
		this.book=book;
		this.date = date;
		dateStr=df.format(date);
		this.setPreferredSize(dim);
		this.setSize(dim);
		l.addAll(book.getListAsks());
		l.addAll(book.getListBids());
		update(l);
	}
	
	public void update(List<OrderBookItem> l){
		
		double maxPrice = l.get(0).getPrice();
		double minPrice = l.get(0).getPrice();
		double amountMAx = l.get(0).getAmount();
		for(OrderBookItem obi : l){
			if (obi.getPrice() > maxPrice) maxPrice= obi.getPrice();
			if (obi.getPrice() < minPrice) minPrice= obi.getPrice();
			if (obi.getAmount()> amountMAx) amountMAx=obi.getAmount();
		}
		double deltaPrice  = maxPrice - minPrice;
		list = new ArrayList<>();
		ammountTotalAsk=0;
		ammountTotalBids=0;
		for(OrderBookItem obi2 : book.getListAsks()){
			int www =(int) ((obi2.getPrice()-minPrice)*w/deltaPrice);
			int hhh =  (int)(((obi2.getAmount())/amountMAx) * h);
			int hh = h - hhh;
			int ww = w - www;
			Item item = new Item(Color.green,ww,hh);
			list.add(item);
			ammountTotalAsk+= obi2.getAmount();
		}
		for(OrderBookItem obi2 : book.getListBids()){
			int www =(int) ((obi2.getPrice()-minPrice)*w/deltaPrice);
			int hhh =  (int)(((obi2.getAmount())/amountMAx) * h);
			int hh = h - hhh;
			int ww = w - www;
			Item item = new Item(Color.red,ww,hh);
			list.add(item);
			ammountTotalBids+= obi2.getAmount();
		}
		ammountTotalAskPercent= (int) (ammountTotalAsk*100/(ammountTotalAsk+ammountTotalBids));
		ammountToyalBidPercent= (int) (ammountTotalBids*100/(ammountTotalAsk+ammountTotalBids));
		label  ="Ask : "+ammountTotalAskPercent+" % Bids "+ammountToyalBidPercent+" %";
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		for (Item item : list ){
			g.setColor(item.color);
			g.fillRect(item.w, item.h, 5, h-item.h);
		}
		g.setColor(Color.BLACK);
		g.setFont(myFont);
		g.drawString(dateStr, 20, 20);
		
		g.drawString(label, 20, 60);
	}

	static class Item {
		Color color;
		int w =0;
		int h;
		public Item(Color color, int ww, int hh) {
			this.color = color;
			this.w = ww;
			this.h = hh;
		}
		
	}
}
