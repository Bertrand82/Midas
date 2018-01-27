package bg.panama.btc.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	String labelAsk="";
	String labelBid="";
	PanelOrderBookHistory parent;
	public PanelOrderBook(Date date, OrderBook book, PanelOrderBookHistory parent) {
		this.book=book;
		this.date = date;
		this.parent = parent;
		dateStr=df.format(date);
		this.setPreferredSize(dim);
		this.setSize(dim);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				drawVerticalLine(e.getX(), e.getY());
			}
		});
	
	}
	double deltaPrice  ;
	double minPrice;
	public void update(double maxPrice, double minPrice, double amountMax){
		System.out.println("update orderBook panel maxPrice :"+maxPrice+"  minPrice :"+minPrice+"   amountMax :"+amountMax);
		this.minPrice = minPrice;
		deltaPrice  = maxPrice - minPrice;
		list = new ArrayList<>();
		
		for(OrderBookItem obi2 : book.getListAsks()){
			int www =(int) ((obi2.getPrice()-minPrice)*w/deltaPrice);
			int hhh =  (int)(((obi2.getAmount())/amountMax) * h);
			int hh = h - hhh;
			int ww = w - www;
			Item item = new Item(Color.green,ww,hh);
			list.add(item);
			
		}
		for(OrderBookItem obi2 : book.getListBids()){
			int www =(int) ((obi2.getPrice()-minPrice)*w/deltaPrice);
			int hhh =  (int)(((obi2.getAmount())/amountMax) * h);
			int hh = h - hhh;
			int ww = w - www;
			Item item = new Item(Color.red,ww,hh);
			list.add(item);
			
		}
		labelAsk  ="Ask : "+book.getAmmountTotalAskPercent()+" % ";
		labelBid  ="Bids : "+book.getAmmountToyalBidPercent()+" %";
		System.out.println("update orderBook panel label :"+labelAsk+labelBid);
		
	}
	private boolean drawVerticalLine = false;
	int x =0;
	double priceClicked ;
	private void drawVerticalLine(int x, int y) {
		this.x = x;
		priceClicked = ((x*deltaPrice)/w)  +minPrice ;		
		drawVerticalLine=true;
		this.parent.setPrice(priceClicked);
		repaint();
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
		g.drawString(this.book.getSymbol()+dateStr, 20, 20);
		g.drawString(this.book.getSymbol()+dateStr, 20, 50);
		g.setColor(Color.GREEN);
		g.drawString(labelAsk, 20, 80);
		g.setColor(Color.RED);
		g.drawString(labelBid, 20, 110);
		if (drawVerticalLine){
			g.setColor(Color.BLACK);
			g.drawLine(x, 0, x, h);
			g.drawString("priceClicked : "+priceClicked, 20,140);
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
