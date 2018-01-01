package btc.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import btc.BitfinexClient;
import btc.BitfinexClient.EnumService;
import btc.model.Symbols;
import btc.trading.first.Config;
import btc.trading.first.SessionCurrencies;
import btc.trading.first.ThreadTrading;


public class MidasGUI {

	
	JLabel labelLog = new JLabel(" ");
	DialogInputKey dialogInputKey;
	
	DialogSelectSymbols dialogSelectSymbols;
	ProtectedConfigFile protectedConfigFile;
	JButton buttonStartThreadThreading = new JButton("Start Thread Trading");
	String password ;
	JCheckBoxMenuItem menuItemSaveConfig= new JCheckBoxMenuItem("Save Password");
	JCheckBoxMenuItem menuItemOrderAble= new JCheckBoxMenuItem("Send Orders");
	JFrame frame = new JFrame("Midas");
	JPanel panelGlobal = new JPanel(new BorderLayout()); 
	private static MidasGUI instance ;
	public MidasGUI() {
		super();
		instance =this;
		Font font = new Font ("Dialog", Font.BOLD, 18);
		UIManager.put("Label.font", font);
		
		buttonStartThreadThreading.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				startThreadTrading();				
			}
		});
		JMenuItem menuSetSecretKeys = new JMenuItem("Set Secret Keys");
		menuSetSecretKeys.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				displaySecretKeys();				
			}
		});
		JMenuItem menuSelectCurrency = new JMenuItem("Select Currency");
		menuSelectCurrency.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				selectCurrencyDialog();				
			}
		});
		
		JMenuItem menuItemEmergencySave = new JMenuItem("Emergency change In $ ");
		menuItemEmergencySave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				emergencySave();
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
		
		JMenu menuFile = new JMenu("File");
		menuFile.add(menuItemSaveConfig);
		menuFile.add(menuItemOrderAble);
		menuFile.add(menuSelectCurrency);
		menuFile.add(menuSetSecretKeys);
		
		JMenu menuActions = new JMenu("Actions");
		menuActions.add(menuItemEmergencySave);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuActions);
		JPanel panelButtons = new JPanel();
		
		//panelButtons.add(buttonFetchSymbols);
		//panelButtons.add(buttonFetchTicker); // Utile pour des test unitaire
		
		panelButtons.add(buttonStartThreadThreading);
		
		
		panelGlobal.add(panelButtons,BorderLayout.NORTH);
		panelGlobal.add(labelLog,BorderLayout.SOUTH);
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panelGlobal, BorderLayout.CENTER);
		frame.setJMenuBar(menuBar);
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
		 password = (String)JOptionPane.showInputDialog(
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
	ThreadTrading threadTrading ;
	private void startThreadTrading() {
		System.out.println("startThreadTrading");
		buttonStartThreadThreading.setEnabled(false);
		boolean orderAble = this.menuItemOrderAble.isSelected();
		Config config = new Config( orderAble,password);
		threadTrading = new ThreadTrading(config);
	}

	public static MidasGUI getInstance() {
		return instance;
	}
	PanelCurrencies panelCurrencies;
	public void updateThread() {
		try {
			SessionCurrencies session = this.threadTrading.getSesionCurrencies();
			if(panelCurrencies == null){
				panelCurrencies = new PanelCurrencies(session);
				panelGlobal.removeAll();
				panelGlobal.add(panelCurrencies);
				frame.pack();
				
			}else {
				panelCurrencies.update(session);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void emergencySave() {
		System.out.println("Emergency Save");
		this.threadTrading.emergencySave("Operator");
	}
}
