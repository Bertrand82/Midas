package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import bg.panama.btc.OrderManager;
import bg.panama.btc.model.OrderBook;
import bg.panama.btc.model.OrderBookFactory;
import bg.panama.btc.model.OrderBookItem;
import bg.panama.btc.swing.History.SimuResult;
import bg.panama.btc.trading.first.Config;
import bg.panama.btc.trading.first.Order;
import bg.panama.btc.trading.first.Order.Side;
import bg.panama.btc.trading.first.SessionCurrency;

public class DialogShowCurrencyDetail extends JPanel {

	
	private static final long serialVersionUID = 1L;
	JFrame frame = new JFrame();
	String name;
	String shortName;
	SessionCurrency session;
	PanelCanvasVariations panelCanvasVariations;
	PanelCanvasPrix panelCanvasPrix;
	PanelCanvasStochastique panelCanvasStochastique_1h;
	PanelCanvasStochastique panelCanvasStochastique_10mn;
	PanelCanvasAchatVente panelCanvasAchatVente;
	PanelCanvasBalanceHistory panelCanvasBalanceHistory;
	PanelOrderBookHistory panelOrderBookHistory = new PanelOrderBookHistory();
	public DialogShowCurrencyDetail(SessionCurrency session, Point location) {

		name = session.getName();
		shortName = session.getShortName();

		panelCanvasPrix = new PanelCanvasPrix(shortName);
		panelCanvasVariations = new PanelCanvasVariations(shortName);
		panelCanvasStochastique_1h = new PanelCanvasStochastique(shortName,
				PanelCanvasStochastique.TypeStochastique.stoc_1heur);
		panelCanvasStochastique_10mn = new PanelCanvasStochastique(shortName,
				PanelCanvasStochastique.TypeStochastique.stoc_10mn);
		panelCanvasAchatVente = new PanelCanvasAchatVente(shortName);
		panelCanvasBalanceHistory = new PanelCanvasBalanceHistory(shortName);
		this.session = session;
		SimuResult s0 = session.getHistory().getSimuResult(0);
		SimuResult s1 = session.getHistory().getSimuResult(1);
		String title = "Detail : " + shortName + "  " + s0.toStringShort() + " " + s1.toStringShort();
		JLabel labelTitle = new JLabel(title);
		labelTitle.setFont(labelTitle.getFont().deriveFont(30));
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				drawVerticalLine(e.getX(), e.getY());
			}

		});
		JButton buttonAcheter = new JButton("Buy");
		JButton buttonVendre = new JButton("Sell");
		JButton buttonShowBook = new JButton("Order Book");
		buttonVendre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(" Vendre "+session.getShortName());
				acheterVendre(Order.Side.sell);
			}

			
		});
		buttonAcheter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Acheter "+session.getShortName());
				acheterVendre(Order.Side.buy);
			}
		});
		buttonShowBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showBookOrder();
			}
		});
		JPanel panelButton = new JPanel(new GridLayout(1, 0));
		panelButton.add(buttonAcheter);		
		panelButton.add(buttonVendre);
		panelButton.add(buttonShowBook);
		JPanel panelNorth = new JPanel(new BorderLayout());
		panelNorth.add(labelTitle, BorderLayout.CENTER);
		panelNorth.add(panelButton, BorderLayout.EAST);

		JPanel panelCenter = new JPanel(new GridLayout(0, 1));
		panelCenter.add(panelCanvasPrix.getJPanel());
		panelCenter.add(panelCanvasVariations.getJPanel());
		panelCenter.add(panelCanvasStochastique_1h.getJPanel());
		panelCenter.add(panelCanvasStochastique_10mn.getJPanel());
		panelCenter.add(panelCanvasAchatVente.getJPanel());
		panelCenter.add(panelCanvasBalanceHistory.getJPanel());
		JPanel panelGlobal = this;
		panelGlobal.setLayout(new BorderLayout());
		panelGlobal.add(panelNorth, BorderLayout.NORTH);
		panelGlobal.add(panelCenter, BorderLayout.CENTER);
		panelGlobal.add(panelOrderBookHistory.getTabbedPane(), BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().add(panelGlobal, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
		frame.setLocation(location);
		frame.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				System.err.println("Hide detail " + name);
				MidasGUI.getInstance().removeDetail(name);
			}

		});
		update(session);
	}

	public void update(SessionCurrency sc) {
		this.panelCanvasPrix.update(sc);
		this.panelCanvasVariations.update(sc);
		this.panelCanvasStochastique_1h.update(sc);
		this.panelCanvasStochastique_10mn.update(sc);
		this.panelCanvasAchatVente.update(sc);
		this.panelCanvasBalanceHistory.update(sc);
	}

	public void setVisible(boolean b) {
		this.frame.setVisible(b);
	}

	public void updateThread(SessionCurrency sc) {
		this.session = sc;
	}

	int x = 0;

	private void drawVerticalLine(int x, int y) {
		this.x = x;
		double timeFromStart  = this.panelCanvasPrix.getDx()*x/this.panelCanvasPrix.w;
		System.out.println("timeFromStart : "+timeFromStart+"   "+(timeFromStart/60000)+" mn    x : "+x);
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.drawLine(x, 20, x, this.getHeight());
		
	}

	private String getSymbolFromName(){
		return this.name.substring(1).toLowerCase();
	}
	
	
	protected void showBookOrder() {

		try {
			String symbol = this.getSymbolFromName();
			OrderBook book = OrderBookFactory.getInstance().getOrderBook(symbol);
			this.panelOrderBookHistory.addOrderBook(book);
			
			this.frame.pack();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void acheterVendre(Side side) {
		double amountInDollard  = Config.getInstance().getPlafondCryptoInDollar();
		double price = this.panelOrderBookHistory.getPrice();
		if (Math.abs(price) < 0.000000001){
			System.err.println("Price Too small");
			JOptionPane.showMessageDialog(frame, "No Price!!!");
			return;
		}
		double amount = amountInDollard/price;
		Order order = new Order(this.session.getShortName(),amount,side,Order.TypeChoicePrice.manual);
		
		order.setPrice(price);
		
		System.out.println("order "+order);
		double lastPrice  = this.session.getTicker_Z_1().getLastPrice();
		int delta =  (int) ((price -lastPrice) *100/price);
		System.out.println(" Last Price : "+lastPrice+"   delta "+delta +" %");
		String message = order.getMessageConfirmation()+" | delta last price ticker : "+delta +" %";
		int result = JOptionPane.showConfirmDialog(frame,message,"Confirm "+order.getSide(), JOptionPane.OK_CANCEL_OPTION);
		System.out.println("result   "+result);
		if (result == JOptionPane.OK_OPTION){
			System.out.println("Okk   envoie l'ordre");
			OrderManager.getInstance().sendOrderPrivate( order);
		}else {
			 return;
		}
	}
}
