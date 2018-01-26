package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

abstract public class PanelCancasAbstract {

	protected final int w = 300;
	protected final int h = 50;
	protected String currency;
	protected Graphics2D g2;
	BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	ImageIcon imageIcon = new ImageIcon(bf);
	
	protected Double xMax;
	protected Double xMin;
	protected Double yMax;
	protected Double yMin;
	protected final double dx = 2 * 60 * 60 * 1000;
	protected  double dy;
	protected double xM;
	protected double yM;


	PanelCancasAbstract(String currency){
		this.currency = currency;
		g2 = bf.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, w, h);
	}
	public Component getJPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(getImageIcon());
		panel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		panel.add(label, BorderLayout.WEST);
		panel.add(getTitre(), BorderLayout.CENTER);
		return panel;
		
	}

	public abstract  ImageIcon getImageIcon() ;

	public abstract JComponent getTitre();
	public double getDx() {
		return dx;
	}

}
