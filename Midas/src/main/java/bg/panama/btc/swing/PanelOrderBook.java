package bg.panama.btc.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import bg.panama.btc.model.OrderBook;
import bg.panama.btc.model.OrderBookItem;

public class PanelOrderBook extends JPanel{

	private static final long serialVersionUID = 1L;
	int w = 500;
	int h = 500;
	Dimension dim = new Dimension(w, h);
	OrderBook book ;
	List<Item> list = new ArrayList<>();
	
	public PanelOrderBook(OrderBook book, List<OrderBookItem> l) {
		this.book=book;
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
		for(OrderBookItem obi2 : book.getListAsks()){
			int ww =(int) ((obi2.getPrice()-minPrice)*w/deltaPrice);
			int hhh =  (int)(((obi2.getAmount())/amountMAx) * h);
			int hh = h - hhh;
			Item item = new Item(Color.green,ww,hh);
			list.add(item);
		}
		for(OrderBookItem obi2 : book.getListBids()){
			int ww =(int) ((obi2.getPrice()-minPrice)*w/deltaPrice);
			int hhh =  (int)(((obi2.getAmount())/amountMAx) * h);
			int hh = h - hhh;
			Item item = new Item(Color.red,ww,hh);
			list.add(item);
		}
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		for (Item item : list ){
			g.setColor(item.color);
			g.fillRect(item.w, item.h, 5, h-item.h);
		}
		
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
