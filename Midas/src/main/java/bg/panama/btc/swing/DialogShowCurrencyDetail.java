package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bg.panama.btc.trading.first.SessionCurrency;

public class DialogShowCurrencyDetail extends JPanel{

	JFrame frame = new JFrame();
	String name ;
	String shortName;
	SessionCurrency session;
	PanelCanvasVariations panelCanvasVariations ;
	PanelCanvasPrix panelCanvasPrix;
	PanelCanvasStochastique panelCanvasStochastique_1h;
	PanelCanvasStochastique panelCanvasStochastique_10mn;
	PanelCanvasAchatVente panelCanvasAchatVente;
	public DialogShowCurrencyDetail(SessionCurrency session) {
		
		name = session.getName();
		shortName = session.getShortName();
		
		panelCanvasPrix = new PanelCanvasPrix(shortName);
		panelCanvasVariations= new PanelCanvasVariations(shortName);
		panelCanvasStochastique_1h = new PanelCanvasStochastique(shortName, PanelCanvasStochastique.TypeStochastique.stoc_1heur);
		panelCanvasStochastique_10mn = new PanelCanvasStochastique(shortName, PanelCanvasStochastique.TypeStochastique.stoc_10mn);
		panelCanvasAchatVente = new PanelCanvasAchatVente(shortName);
		this.session = session;
		JLabel labelTitle = new JLabel("Detail : "+shortName);
		labelTitle.setFont(labelTitle.getFont().deriveFont(30));
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				drawVerticalLine(e.getX(),e.getY());
			}
			
		});
		JPanel panelCenter = new JPanel(new GridLayout(0, 1));
		panelCenter.add(panelCanvasPrix.getJPanel());
		panelCenter.add(panelCanvasVariations.getJPanel());
		panelCenter.add(panelCanvasStochastique_1h.getJPanel());
		panelCenter.add(panelCanvasStochastique_10mn.getJPanel());
		panelCenter.add(panelCanvasAchatVente.getJPanel());
		JPanel panelGlobal = this;
		panelGlobal.setLayout(new BorderLayout());
		panelGlobal.add(labelTitle,BorderLayout.NORTH);
		panelGlobal.add(panelCenter,BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().add(panelGlobal, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);

		frame.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				System.err.println("Hide detail "+name);
				MidasGUI.getInstance().removeDetail(name);
			}

		});
		update(session);
	}
	

	public void update(SessionCurrency sc){
		this.panelCanvasPrix.update(sc);
		this.panelCanvasVariations.update(sc);
		this.panelCanvasStochastique_1h.update(sc);
		this.panelCanvasStochastique_10mn.update(sc);
		this.panelCanvasAchatVente.update(sc);
	}
	
	public void setVisible(boolean b) {
		this.frame.setVisible(b);
	}
	public void updateThread(SessionCurrency sc) {
		this.session = sc;
	}

	int x=0;
	private void drawVerticalLine(int x, int y) {
		this.x = x;
		repaint();
	}
	
	 public void paint(Graphics g) {
		 super.paint(g);
		 g.setColor(Color.BLACK);
		 g.drawLine(x, 20, x, this.getHeight());
	 }
	
	
}
