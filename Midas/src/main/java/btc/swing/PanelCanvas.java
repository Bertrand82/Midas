package btc.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import btc.CourbeTestGUI;

import btc.util.PointBtc;

public class PanelCanvas {
	int w = 160;
	int h = 50;
	String currency;
	Graphics2D g2;

	BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	ImageIcon imageIcon = new ImageIcon(bf);

	public PanelCanvas(String currency) {
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

		for (PointBtc p : h.getListPoints()) {
			initMinMax(p);

		}
		System.err.println(" xMax: " + xMax + " xMin: " + xMin + " xM:  " + xM + "  dx: " + dx + " dy: " + dy
				+ "   yMax :" + yMax + "  yMin : " + yMin);
	}

	void initMinMax(PointBtc p) {
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

		//dx = xMax - xMin;
		dx = 60*60*1000;
		//dy = yMax - yMin;
		dy = 2.2;
		xM = (xMax + xMin) / 2;
		yM = (yMax + yMin) / 2;
		yMax=1.0;
		yMin =-1.0;
	}

	public void update(String str, Color color, History history) {
		initFromHistory(history);
		g2.setColor(color);
		g2.fillRect(0, 0, w, h);
		g2.setColor(Color.black);
		g2.drawString(str, 10, 20);
		g2.setColor(Color.black);
		g2.drawLine(0, h / 2, w, h / 2);
		g2.setColor(Color.BLACK);
		for (PointBtc p : history.getListPoints()) {
			if ((dx > 0.000000001) && (dy > 0.000000001)) {
				int x = (int) ((((p.x - xMin) / dx)) * w);
				int y = (int) ((-p.y + yMax) / dy * h)+h/2;
				System.err.println("x :"+x+" y :"+y+"  p.y :"+p.y);
				g2.fillOval(x, y, 4, 4);
			}
		}

	}

}
