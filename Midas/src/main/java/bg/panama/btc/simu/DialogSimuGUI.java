package bg.panama.btc.simu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogSimuGUI {

	JFrame frame = new JFrame();
	SimuProcess simuProcess = new SimuProcess(this);
	JLabel labelLog = new JLabel("");

	/**
	* 
	*/
	public DialogSimuGUI() {
		JButton buttonStart = new JButton("Start");
		buttonStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startSimu();
			}
		});
		JPanel panelNorth = new JPanel();
		panelNorth.add(buttonStart);
		JPanel panelCenter = new JPanel(new GridLayout(0, 1));
		JPanel panelGlobal = new JPanel(new BorderLayout());
		panelGlobal.add(panelNorth, BorderLayout.NORTH);
		panelGlobal.add(panelCenter, BorderLayout.CENTER);
		panelGlobal.add(labelLog, BorderLayout.SOUTH);
		frame.setTitle("Simulation");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().add(panelGlobal, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		frame.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				stopSimu();
			}
		});

	}

	public void setVisible(boolean b) {
		this.frame.setVisible(b);
	}

	private void startSimu() {
		System.err.println("Start Simu ");
		this.simuProcess.startSimuProcess();
	}

	public void stopSimu() {
		System.err.println("Stop Simu No Implemented yet");
	}

	public void log(String s) {
		this.labelLog.setText(""+s);
	}
}
