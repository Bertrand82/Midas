package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bg.panama.btc.trading.first.SessionCurrency;

public class DialogShowCurrency {

	JFrame frame = new JFrame();
	String name ;
	String shortName;
	public DialogShowCurrency(SessionCurrency session) {
		name = session.getName();
		shortName = session.getShortName();
		JLabel labelTitle = new JLabel("Detail : "+name);
		JPanel panelGlobal = new JPanel();
		panelGlobal.add(labelTitle);
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
	}
	public void setVisible(boolean b) {
		this.frame.setVisible(b);
	}

}
