package btc.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelCanvas {
	int width =160;
	int height = 50;
	String currency;
	Graphics2D g2;
	
	BufferedImage bf= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	ImageIcon imageIcon  = new ImageIcon(bf);
	
	public PanelCanvas( String  currency) {
		super();
		this.currency = currency;
		
		g2 = bf.createGraphics();
		g2.setColor(Color.CYAN);
		g2.fillRect(0, 0, width, height);
	}
	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	
	public void update(String str, Color color){
		g2.setColor(color);
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.black);
		g2.drawString(str, 10, 20);
		
	}
	
	
	

}
