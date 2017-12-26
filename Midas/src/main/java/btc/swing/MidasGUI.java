package btc.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import btc.BitfinexClient;
import btc.BitfinexClient.EnumService;
import btc.model.Symbols;

public class MidasGUI {

	JLabel labelLog = new JLabel(" ");
	DialogInputKey dialogInputKey;
	DialogSelectSymbols dialogSelectSymbols;
	ProtectedConfigFile protectedConfigFile;
	public MidasGUI() {
		super();
		JButton buttonStartThreadThreading = new JButton("Start Thread Trading");
		buttonStartThreadThreading.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				startThreadTrading();				
			}
		});
		JButton buttonSetSecretKeys = new JButton("Set Secret Keys");
		buttonSetSecretKeys.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				displaySecretKeys();				
			}
		});
		JButton buttonSelectCurrency = new JButton("Select Currency");
		buttonSelectCurrency.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				selectCurrencyDialog();				
			}
		});
		JButton buttonFetchSymbols = new JButton("Fetch symbols");
		buttonFetchSymbols.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				fetchSymbols();				
			}
		});
		JButton buttonFetchTicker = new JButton("Fetch ticker");
		buttonFetchTicker.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				fetchTickers();				
			}
		});
		JPanel panelButtons = new JPanel();
		panelButtons.add(buttonSelectCurrency);
		panelButtons.add(buttonFetchSymbols);
		panelButtons.add(buttonFetchTicker);
		panelButtons.add(buttonSetSecretKeys);
		panelButtons.add(buttonStartThreadThreading);
		
		JPanel panelGlobal = new JPanel(new BorderLayout()); 
		panelGlobal.add(panelButtons,BorderLayout.NORTH);
		panelGlobal.add(labelLog,BorderLayout.SOUTH);
        JFrame frame = new JFrame("Midas");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panelGlobal, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		dialogSelectSymbols = new DialogSelectSymbols(frame, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				processSymbolsSelected();
			}
		});
		dialogInputKey = new DialogInputKey(frame, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				configureBitFinexKey();
			}
		});
		String password = (String)JOptionPane.showInputDialog(
                frame,
                "Password:\n",
                "Properties Password",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "mypassword");
		try {
			this.protectedConfigFile = new ProtectedConfigFile(password);
		} catch (Exception e1) {
			this.labelLog.setText("Exception config");
			e1.printStackTrace();
		}

	}

	private void fetchTickers(){
		try {
			labelLog.setText("fetchTickers");
			BitfinexClient bitfinexClient= new BitfinexClient("", "");
			String devises =SymbolsConfig.getInstance().getSymbolsSelectedRequest();
			Object o = bitfinexClient.serviceProcess(EnumService.tickersV2,"",devises);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void configureBitFinexKey(){
		this.labelLog.setText("Configure bitfinex keys ");
		try {
			String apiKey = this.dialogInputKey.getApiKey();
			String secretKey = this.dialogInputKey.getSecretKey();
			protectedConfigFile.set(ProtectedConfigFile.keyBifinexApiKey, apiKey);
			protectedConfigFile.set(ProtectedConfigFile.keyBitfinexSecretKey, secretKey);
		} catch (Exception e) {
			this.labelLog.setText("Exception "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private void displaySecretKeys() {
		this.dialogInputKey.setVisible(true);
	}
	
	private void fetchSymbols(){
		try {
			labelLog.setText("fetchSimbols");
			BitfinexClient bitfinexClient= new BitfinexClient("", "");
			Object o = bitfinexClient.serviceProcess(EnumService.symbols,"","");
			Symbols symbols =(Symbols) o;
			List<String>  listUsd =symbols.getAllEndWith("usd");
			for(String s :listUsd){
				System.out.println("symbol :::: "+s);
			}
			System.err.println("listUsd.size : "+listUsd.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void selectCurrencyDialog(){
		this.dialogSelectSymbols.setVisible(true);
	}
	
	private void processSymbolsSelected(){
		System.out.println("Process Symbol Selected");
		
	}
	
	private void startThreadTrading() {
		System.out.println("startThreadTrading");
	}
	
}
