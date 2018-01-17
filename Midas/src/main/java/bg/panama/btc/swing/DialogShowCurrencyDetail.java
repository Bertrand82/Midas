package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bg.panama.btc.trading.first.SessionCurrency;

public class DialogShowCurrencyDetail {

	JFrame frame = new JFrame();
	String name ;
	String shortName;
	SessionCurrency session;
	PanelCanvasVariations panelCanvasVariations ;
	PanelCanvasPrix panelCanvasPrix;
	PanelCanvasStochastique panelCanvasStochastique_1h;
	PanelCanvasStochastique panelCanvasStochastique_10mn;
	public DialogShowCurrencyDetail(SessionCurrency session) {
		
		name = session.getName();
		shortName = session.getShortName();
		
		panelCanvasPrix = new PanelCanvasPrix(shortName);
		panelCanvasVariations= new PanelCanvasVariations(shortName);
		panelCanvasStochastique_1h = new PanelCanvasStochastique(shortName, PanelCanvasStochastique.TypeStochastique.stoc_1heur);
		panelCanvasStochastique_10mn = new PanelCanvasStochastique(shortName, PanelCanvasStochastique.TypeStochastique.stoc_10mn);
		this.session = session;
		JLabel labelTitle = new JLabel("Detail : "+shortName);
		labelTitle.setFont(labelTitle.getFont().deriveFont(30));
		
		JPanel panelCenter = new JPanel(new GridLayout(0, 1));
		panelCenter.add(panelCanvasPrix.getJPanel());
		panelCenter.add(panelCanvasVariations.getJPanel());
		panelCenter.add(panelCanvasStochastique_1h.getJPanel());
		panelCenter.add(panelCanvasStochastique_10mn.getJPanel());
		JPanel panelGlobal = new JPanel(new BorderLayout());
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
	}
	
	public void setVisible(boolean b) {
		this.frame.setVisible(b);
	}
	public void updateThread(SessionCurrency sc) {
		this.session = sc;
	}

}
