package bg.panama.btc.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import bg.panama.btc.trading.first.SessionCurrency;
import bg.util.PointDouble;

public class PanelCanvasStochastique  extends PanelCancasAbstract {


	public enum TypeStochastique {
		stoc_1heur,
		stoc_10mn
	}
	
		TypeStochastique type;
	public PanelCanvasStochastique(String currency, TypeStochastique type) {
		super(currency);
		this.type = type;
		
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	

	private List<PointDouble> getPoints(History h){
		if (type == TypeStochastique.stoc_1heur){
			return h.getListPointsStochastiques_1heure() ;
		}
		if (type == TypeStochastique.stoc_10mn){
			return h.getListPointsStochastiques_10mn();
		}
		System.err.println("Big problem 727 type:"+type);
		return null;
	}
	private void initFromHistory(History h) {
		xMax = null;
		for (PointDouble p : getPoints(h)) {
			initMinMax(p);
		}
	}

	void initMinMax(PointDouble p) {

		if (xMax == null) {
			xMax = p.x;
			xMin = p.x;
			yMax = p.y;
			yMin = p.y;
		}
		if (p.x > xMax) {
			xMax = p.x;
		}
		
		if (p.x < xMin) {
			xMin = p.x;
		}
		

		// dx = xMax - xMin;
		xMin = xMax -dx;
		// dy = yMax - yMin;
		yMax = 100d;
		yMin = 0d;
		dy = 100d;

		xM = (xMax + xMin) / 2;
		yM = (yMax + yMin) / 2;

	}

	public static final Color GREEN = new Color(0x99FF66);
	public static final Color RED = new Color(0xFF0066);

	public void update(SessionCurrency sc) {
		update( sc.getHistory());
	}
	public void update(History history) {
		initFromHistory(history);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, w, h);
		
		g2.setColor(Color.gray);
		g2.drawLine(0, h / 2, w, h / 2);

		
		for (PointDouble p : getPoints(history)) {
			if ((dx > 0.000000001) && (dy > 0.000000001)) {
				int x = (int) ((((p.x - xMin) / dx)) * w);
				double y_y = (p.y-yMin) / dy * h;
				int y = (int) ((h - y_y) *0.9);
				g2.setColor(Color.BLACK);
				g2.fillOval(x, y, 4, 4);
				//System.out.println("Prix Filtre:\t"+str+"\t x: " + x + "  y :" + y);
			}
		}
	}

	@Override
	public JComponent getTitre() {
		String s = "Stochastique";
		if (type == TypeStochastique.stoc_10mn){
			s +=" 10 mn";
		}else if (type == TypeStochastique.stoc_1heur){
			s +=" 1 h";
		}
		return new JLabel(s);
	}

	

}
