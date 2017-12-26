package btc.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogInputKey extends JDialog implements ActionListener {
	
	
	JTextField textField_APIKEY = new JTextField(20);
	JTextField textField_SecretKEY = new JTextField(20);
	
	private static final long serialVersionUID = 1L;
	
	public DialogInputKey(JFrame parent,ActionListener okListener) {
	    super(parent, "Bitfinex", true);
	    if (parent != null) {
	      Dimension parentSize = parent.getSize(); 
	      Point p = parent.getLocation(); 
	      setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
	    }
	    JPanel messagePane = new JPanel(new GridLayout(2, 2));
	    messagePane.add(new JLabel("API Key"));
	    messagePane.add(textField_APIKEY);
	    messagePane.add(new JLabel("Secret Key"));
	    messagePane.add(textField_SecretKEY);
	    getContentPane().add(messagePane);
	    JPanel buttonPane = new JPanel();
	    JButton button = new JButton("CANCEL"); 
	    JButton buttonOk = new JButton("OK");
	    buttonOk.addActionListener(this);
	    buttonOk.addActionListener(okListener);
	    buttonPane.add(button); 
	    buttonPane.add(buttonOk); 
	    button.addActionListener(this);
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack(); 
	    setVisible(false);
	  }
	  public void actionPerformed(ActionEvent e) {
	    setVisible(false); 
	    dispose(); 
	  }
	  
	  
	  public static void main(String[] a) {
	    DialogInputKey dlg = new DialogInputKey(new JFrame(),null);
	    dlg.setVisible(true);
	  }
	public String getApiKey() {
		return this.textField_APIKEY.getText();
	}
	public String getSecretKey() {
		return this.textField_SecretKEY.getText();
	}
	}





	 

	 



