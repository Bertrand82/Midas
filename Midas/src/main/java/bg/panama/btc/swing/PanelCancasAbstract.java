package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

abstract public class PanelCancasAbstract {

	
	public Component getJPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(getImageIcon());
		panel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		panel.add(label, BorderLayout.WEST);
		panel.add(new JLabel(getTitre()), BorderLayout.CENTER);
		return panel;
		
	}

	public abstract  ImageIcon getImageIcon() ;

	public abstract String getTitre();

}
