package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogInputPassword extends JDialog implements ActionListener {
	
	
	JTextField textField_password = new JTextField("mypassword",20);
	JCheckBox checkboxSavePassword = new JCheckBox("");
	
	private static final long serialVersionUID = 1L;
	
	public DialogInputPassword(JFrame parent,ActionListener okListener) {
	    super(parent, "Bitfinex", true);
	    
	    if (parent != null) {
	      Dimension parentSize = parent.getSize(); 
	      Point p = parent.getLocation(); 
	      setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
	    }
	    JPanel messagePane = new JPanel(new GridLayout(2, 2));
	    messagePane.add(new JLabel("Password : "));
	    messagePane.add(textField_password);
	    messagePane.add(new JLabel("Save Password"));
	    messagePane.add(checkboxSavePassword);
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
	    DialogInputPassword dlg = new DialogInputPassword(new JFrame(),null);
	    dlg.setVisible(true);
	  }
	public String getPassword() {
		return this.textField_password.getText();
	}
	public boolean isPasswordSavable() {
		return this.checkboxSavePassword.isSelected();
	}
	}





	 

	 



