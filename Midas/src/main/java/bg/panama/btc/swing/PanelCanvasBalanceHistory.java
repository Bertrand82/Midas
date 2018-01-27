package bg.panama.btc.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import bg.panama.btc.trading.first.SessionCurrency;
import bg.util.PointDouble;
import bg.util.PointTriple;

public class PanelCanvasBalanceHistory extends PanelCancasAbstract{

	
	public PanelCanvasBalanceHistory(String currency) {
		super(currency);
		
	
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	

	private void initFromHistory(History h) {
		xMax = null;
		for (PointTriple p : h.getListPointsBalanceHistory() ){
			initMinMax(p);
		}
	}

	void initMinMax(PointTriple p) {

		if (xMax == null) {
			xMax = p.x;
			xMin = p.x;
			yMax = p.y;
			yMin = p.y;
		}
		if (p.x > xMax) {
			xMax = p.x;
		}
		if (p.y > yMax) {
			yMax = p.y;
		}
		if (p.x < xMin) {
			xMin = p.x;
		}
		if (p.y < yMin) {
			yMin = p.y;
		}

		// dx = xMax - xMin;
		xMin = xMax -dx;
		// dy = yMax - yMin;
		
		dy = yMax*1.5;

	
	}

	public static final Color GREEN = new Color(0x006400);
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

		for (PointTriple p : history.getListPointsBalanceHistory()) {
			if ((dx > 0.000000001) && (dy > 0.000000001)) {
				int x = (int) ((((p.x - xMin) / dx)) * w);
				int y = (int) ((((-p.y) / dy * h)+h) * 0.9);
				g2.setColor(Color.BLUE);
				g2.fillOval(x, y, 4, 4);
				if(p.o1 == null){
				}else if(((Double) p.o1) > 0){
					g2.setColor(Color.GREEN);
					g2.drawLine(x, 0, x, h);
					g2.fillOval(x-3,5, 6, 6);
				}
				if(p.o2 == null){
				} else if(((Double) p.o2) > 0){
					g2.setColor(Color.RED);
					g2.drawLine(x, 0, x, h);
					g2.fillOval(x-3,15, 6,6);
				}
			}
		}
		
	}

	
	public  JComponent getTitre() {
		return new JLabel("Amount  $");
	}

	

}
