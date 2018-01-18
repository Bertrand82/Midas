package bg.panama.btc.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.hibernate.internal.SessionOwnerBehavior;

import bg.panama.btc.trading.commun.Value;
import bg.panama.btc.trading.first.SessionCurrency;
import bg.util.PointDouble;

public class PanelCanvasAchatVente extends PanelCancasAbstract {

	public enum TypeStochastique {
		stoc_1heur, stoc_10mn
	}

	int w = 300;
	int h = 50;
	String currency;
	Graphics2D g2;

	BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	ImageIcon imageIcon = new ImageIcon(bf);

	public PanelCanvasAchatVente(String currency) {
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

	List<Value> listVAlues = new ArrayList<>();

	private void initFromHistory(History h) {
		xMax = null;
		listVAlues = h.getStochastiques_10mn();
		for (Value p : h.getStochastiques_10mn()) {
			initMinMax(p);
		}
	}

	void initMinMax(Value p) {

		if (xMax == null) {
			xMax = (double) p.time;
			xMin = (double) p.time;

		}
		if (p.time !=0){
		if (p.time > xMax) {
			xMax = (double) p.time;
		}if (p.time < xMin) {
			xMin = (double) p.time;
		}
		}
		// dx = xMax - xMin;
		dx = 2 * 60 * 60 * 1000;
		
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
		update(sc.getHistory());
	}

	public void update(History history) {
		initFromHistory(history);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, w, h);

		g2.setColor(Color.gray);
		g2.drawLine(0, h / 2, w, h / 2);

		for (Value p : this.listVAlues) {
			if ((dx > 0.000000001) && (dy > 0.000000001)) {
				SessionCurrency.Etat_stochastique etat= SessionCurrency.getStochastique(p);
				boolean draw = false;
				if (etat == SessionCurrency.Etat_stochastique.down_up) {
					g2.setColor(Color.GREEN);
					draw = true;
				}else if (etat == SessionCurrency.Etat_stochastique.up_down) {
					g2.setColor(Color.RED);
					draw = true;
				}else if (etat == SessionCurrency.Etat_stochastique.up_up) {
					g2.setColor(Color.YELLOW);
					draw = true;
				}
				if (draw){
					int x = (int) ((p.time - xMin)*w/dx);
					g2.drawLine(x, 0, x, 20);
					System.err.println("drawLine   x:"+x+"  ");
				}
				// System.out.println("Prix Filtre:\t"+str+"\t x: " + x + " y :"
				// + y);
			}
		}
	}

	@Override
	public String getTitre() {
		String s = "Achat Vente";
		return s;
	}

	class PointAchatVente {

	}

}
