package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
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
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.Balance;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.Symbols;
import bg.panama.btc.trading.first.AlgoProcessCurrencies;
import bg.panama.btc.trading.first.Config;
import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrency;
import bg.panama.btc.trading.first.ThreadBalance;
import bg.panama.btc.trading.first.ThreadFetchTickers;
import bg.panama.btc.trading.first.ThreadProcessTickers;


public class MidasGUI {

	
	JLabel labelLog = new JLabel(" ");
	DialogInputKey dialogInputKey;
	
	DialogSelectSymbols dialogSelectSymbols;
	ConfigFileProtected configFileProtected;
	JButton buttonStartThreadThreading_OLD = new JButton("Start Thread Trading");
	String password ;
	JCheckBoxMenuItem menuItemSaveConfig= new JCheckBoxMenuItem("Save Password");
	JCheckBoxMenuItem menuItemOrderAble= new JCheckBoxMenuItem("Send Orders");
	JFrame frame = new JFrame("Midas");
	JPanel panelGlobal = new JPanel(new BorderLayout()); 
	ThreadFetchTickers threadFetchTickers ;
	ThreadProcessTickers threadProcessTickers;
	ThreadBalance threadBalance;
	private static MidasGUI instance ;
	public MidasGUI() {
		super();
		instance =this;
		Font font = new Font ("Dialog", Font.BOLD, 18);
		UIManager.put("Label.font", font);
		
		
		JMenuItem menuSetSecretKeys = new JMenuItem("Set Secret Keys");
		menuSetSecretKeys.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				displayDialogInputKey();				
			}
		});
		JMenuItem menuSelectCurrency = new JMenuItem("Select Currency");
		menuSelectCurrency.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				selectCurrencyDialog();				
			}
		});
		JMenuItem menuItemDisplayAllOrders = new JMenuItem("Display All Orders  ");
		menuItemDisplayAllOrders.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				displayActiveOrders();
			}			
		});
		
		JMenuItem menuItemCancelAllOrders = new JMenuItem("Cancel All Orders  ");
		menuItemCancelAllOrders.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAllOrders();
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
		menuItemOrderAble.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (threadFetchTickers != null){
					boolean b = menuItemOrderAble.isSelected();
					System.err.println("orderAble::  "+b);
					threadFetchTickers.getConfig().setOrderAble(b);
				}
				
			}
		});
		
		JMenuItem menuSetPassword = new JMenuItem("setPassword2");
		menuSetPassword.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.err.println("setpassword start");
				 password = (String)JOptionPane.showInputDialog(
			                frame,
			                "Password:\n",
			                "Properties Password",
			                JOptionPane.PLAIN_MESSAGE,
			        		
			                null,
			                null,
			                "mypassword");
				try {
					MidasGUI.this.configFileProtected = new ConfigFileProtected(password);
				} catch (Exception e1) {
					MidasGUI.this.labelLog.setText("Exception config");
					e1.printStackTrace();
				}	
				startAuthenticatedThread();
			}
		});
		
		JMenu menuFile = new JMenu("File");
		menuFile.add(menuItemSaveConfig);
		menuFile.add(menuItemOrderAble);
		menuFile.add(menuSelectCurrency);
		menuFile.add(menuSetSecretKeys);
		
		JMenu menuActions = new JMenu("Actions");
		menuActions.add(menuItemCancelAllOrders);
		menuActions.add(menuItemDisplayAllOrders);
		menuActions.add(menuItemEmergencySave);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuActions);
		menuBar.add(menuSetPassword);
		JPanel panelButtons = new JPanel();
		
		//panelButtons.add(buttonFetchSymbols);
		//panelButtons.add(buttonFetchTicker); // Utile pour des test unitaire
		
	//	panelButtons.add(buttonStartThreadThreading);
		
		
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
		
		startThreads();
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
			configFileProtected.set(ConfigFileProtected.keyBifinexApiKey, apiKey);
			configFileProtected.set(ConfigFileProtected.keyBitfinexSecretKey, secretKey);
		} catch (Exception e) {
			this.labelLog.setText("Exception "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private void displayDialogInputKey() {
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
	
	private void startThreads() {
		threadFetchTickers = new ThreadFetchTickers(getConfig());
		threadProcessTickers = new ThreadProcessTickers();
	}
	
	private Config getConfig() {
		boolean orderAble = this.menuItemOrderAble.isSelected();
		Config config = new Config( orderAble,password);
		return config;
	}

	public static MidasGUI getInstance() {
		return instance;
	}
	PanelCurrencies panelCurrencies;
	public void updateThread() {
		try {
			SessionCurrencies session = this.threadFetchTickers.getSesionCurrencies();
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
		this.threadFetchTickers.emergencySave("Operator");
	}
	
	private void cancelAllOrders() {
		try {
			System.out.println("Cancel Order");
			this.threadFetchTickers.cancelAllOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void displayActiveOrders() {
		try {
			System.out.println("Cancel Order");
			this.threadFetchTickers.displayActiveOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateBestCurrencies(AlgoProcessCurrencies algoProcess) {
		if (this.panelCurrencies== null){
			
		}else {
			this.panelCurrencies.updateAlgo(algoProcess);
		}
	}
	
	public void updateThreadBalance(Balances balances) {
		this.panelCurrencies.updateBalances(balances);
	}
	
	private void startAuthenticatedThread(){
		if (this.threadBalance != null){
			threadBalance.stop("GUI");
		}		
		this.threadBalance = new ThreadBalance(getConfig());
	}

	HashMap<String, DialogShowCurrency> hDetails = new HashMap<>();
	public void showDetail(SessionCurrency session, boolean b) {
		String name = session.getName();
		System.err.println("Show Detail "+name);
		DialogShowCurrency dialogShowCurrency = hDetails.get(name);
		if ( dialogShowCurrency== null){
			 dialogShowCurrency = new DialogShowCurrency(session);
			hDetails.put(name, dialogShowCurrency);
		}else {
			 dialogShowCurrency.setVisible(b);
		}
	}

	public void removeDetail(String name) {
		hDetails.remove(name);
	}


}
