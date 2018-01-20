package bg.panama.btc.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import bg.panama.btc.trading.first.SessionCurrency;
import bg.util.PointDouble;

public class PanelCanvasVariations  extends PanelCancasAbstract{
	int w = 300;
	int h = 50;
	String currency;
	Graphics2D g2;

	BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	ImageIcon imageIcon = new ImageIcon(bf);

	public PanelCanvasVariations(String currency) {
		super();
		this.currency = currency;

		g2 = bf.createGraphics();
		g2.setColor(Color.CYAN);
		g2.fillRect(0, 0, w, h);
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	private Double xMax;
	private Double xMin;
	private Double yMax;
	private Double yMin;
	private double xM;
	private double yM;
	private double dx;
	private double dy;

	private void initFromHistory(History h) {
		xMax= null;
		for (PointDouble p : h.getListPointsVariation()) {
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
		dx = 2*60*60*1000;
		dy = 20;
		xMin = xMax -dx;
		
		yMax=10.0;
		yMin =-10.0;
		
		xM = (xMax + xMin) / 2;
		yM = (yMax + yMin) / 2;
	}
   public static final Color GREEN = new Color(0xB0FF70);
 public static final Color RED = new Color(0xFF70A0);
 
 public void update(SessionCurrency sc) {
	update(sc.getHistory());
	}
   public void update(  History history) {
		initFromHistory(history);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, w, h);
		
		
		
		for (PointDouble p : history.getListPointsVariation()) {
			if ((dx > 0.000000001) && (dy > 0.000000001)) {
				int x = (int) ((((p.x - xMin) / dx)) * w) ;
				int y = (int) (  (((-p.y ) / dy * h)+h/2)*0.9);
				Color color2;
				if(p.y > 0){
					color2=GREEN;
				}else {
					color2=RED;
				}
				
				g2.setColor(color2);
				g2.fillRect(x+1, 0, 4, h);
				g2.setColor(Color.BLACK);
				g2.fillOval(x, y, 4, 4);
			}
		}
		g2.setColor(Color.black);
		g2.drawLine(0, h / 2, w, h / 2);

	}

@Override
public JComponent getTitre() {
	
	return new JLabel("Variations");
}



}
