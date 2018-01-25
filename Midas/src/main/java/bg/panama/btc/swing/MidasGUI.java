package bg.panama.btc.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.Symbols;
import bg.panama.btc.simu.DialogSimuGUI;
import bg.panama.btc.trading.first.AlgoProcessCurrencies;
import bg.panama.btc.trading.first.Config;
import bg.panama.btc.trading.first.Order;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrency;
import bg.panama.btc.trading.first.ThreadBalance;
import bg.panama.btc.trading.first.ThreadFetchTickers;
import bg.panama.btc.trading.first.ThreadProcessTickers;

public class MidasGUI {

	JLabel labelLog = new JLabel("Kabel log !!!!!  ");
	DialogInputKey dialogInputKey;
	DialogSelectSymbols dialogSelectSymbols;
	ConfigFileProtected configFileProtected;
	JButton buttonStartThreadThreading_OLD = new JButton("Start Thread Trading");
	String password;
	JCheckBoxMenuItem menuItemSaveConfig_____DEPRECATED = new JCheckBoxMenuItem("Save Password");
	JCheckBoxMenuItem menuItemOrderAble = new JCheckBoxMenuItem("Send Orders");
	JCheckBoxMenuItem menuItemOrderAbleAchat = new JCheckBoxMenuItem("Send Orders achat");
	JCheckBoxMenuItem menuItemOrderAbleVente = new JCheckBoxMenuItem("Send Orders vente");
	JFrame frame = new JFrame("Midas");
	JPanel panelGlobal = new JPanel(new BorderLayout());
	ThreadFetchTickers threadFetchTickers;
	ThreadProcessTickers threadProcessTickers;
	ThreadBalance threadBalance;
	private static MidasGUI instance;
	JMenu menuPanic = new JMenu("PANIC");
	JCheckBox checkBoxSavePassword;
	JTextField textFieldPlafondMaxDollar = new JTextField("00000");
	public MidasGUI() {
		super();
		instance = this;
		Font font = new Font("Dialog", Font.BOLD, 18);
		UIManager.put("Label.font", font);
		this.checkBoxSavePassword = new JCheckBox("Save Password ", Config.getInstance().getSavePassword());
		menuItemOrderAble.setSelected(Config.getInstance().isOrderAble());
		menuItemOrderAbleAchat.setSelected(Config.getInstance().isOrderAbleAchat());
		menuItemOrderAbleVente.setSelected(Config.getInstance().isOrderAbleVente());
		textFieldPlafondMaxDollar.setText(""+Config.getInstance().getPlafondCryptoInDollar());
		JMenuItem menuTestBestCurrency = new JMenuItem("Test Best Currency");
		menuTestBestCurrency.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				testBestCurrency();
			}
		});
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

		JMenuItem menuRemovePanic = new JMenuItem("Remove Panic  ");
		menuRemovePanic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removePanic();
			}
		});

		JMenuItem menuItemCancelAllOrders = new JMenuItem("Cancel All Orders  ");
		menuItemCancelAllOrders.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAllOrders();
			}
		});

		JMenuItem menuItemStartSimu = new JMenuItem("Start Simu ");
		menuItemStartSimu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startSimu();
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
		JMenuItem menuPlafondMaximalCrypto = new JMenuItem("Plafond Maximal Crypto");
		menuPlafondMaximalCrypto.addActionListener( e->{
			JOptionPane.showMessageDialog(frame, textFieldPlafondMaxDollar);
			int plafondCryptoInDollar = Integer.parseInt(textFieldPlafondMaxDollar.getText());
			System.out.println("plafond "+plafondCryptoInDollar);
			Config.getInstance().setPlafondCryptoInDollar(plafondCryptoInDollar);
			log("Plafond Crypto "+plafondCryptoInDollar+" $");
			});
		
		menuItemOrderAble.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					boolean orderAble = menuItemOrderAble.isSelected();
					System.err.println("orderAble::  " + orderAble);
					Config.getInstance().setOrderAble(orderAble);
			}
		});

		menuItemOrderAbleAchat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					boolean orderAbleAchat = menuItemOrderAbleAchat.isSelected();
					System.err.println("orderAbleAchat::  " + orderAbleAchat);
					Config.getInstance().setOrderAbleAchat(orderAbleAchat);
			}
		});
		menuItemOrderAbleVente.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					boolean orderAbleVente = menuItemOrderAbleVente.isSelected();
					System.err.println("orderAble::  " + orderAbleVente);
					Config.getInstance().setOrderAbleVente(orderAbleVente);
			}
		});
		setPanic(false, 0, 0);
		JMenu menuAuthentification = new JMenu("Authentification");
		JMenuItem menuSetPassword = new JMenuItem("Set Password");
		JMenuItem menuRemovePassword = new JMenuItem("Remove Password");
		JMenuItem menuSavePassword = new JMenuItem("Save Password");

		menuAuthentification.add(menuSetPassword);
		menuAuthentification.add(menuRemovePassword);
		menuAuthentification.add(menuSavePassword);

		menuSavePassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("SavePAssword");

				JOptionPane.showMessageDialog(frame, checkBoxSavePassword);
				boolean savePassword = checkBoxSavePassword.isSelected();
				System.out.println("SavePassword done " + savePassword);
				Config.getInstance().setSavePassword(savePassword);
			}
		});
		menuRemovePassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.err.println("Remove password");
				ConfigFileProtected.removeInstance();
			}
		});
		menuSetPassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.err.println("setpassword start");
				password = (String) JOptionPane.showInputDialog(frame, "Password:\n", "Properties Password",
						JOptionPane.PLAIN_MESSAGE, null, null, "mypassword");
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
		menuFile.add(menuItemOrderAble);
		menuFile.add(menuItemOrderAbleAchat);
		menuFile.add(menuItemOrderAbleVente);
		menuFile.add(menuPlafondMaximalCrypto);
		menuFile.add(menuSelectCurrency);
		menuFile.add(menuSetSecretKeys);
		menuFile.add(menuTestBestCurrency);

		this.menuPanic.add(menuRemovePanic);

		JMenu menuActions = new JMenu("Actions");
		menuActions.add(menuItemCancelAllOrders);
		menuActions.add(menuItemDisplayAllOrders);
		menuActions.add(menuItemEmergencySave);
		menuActions.add(menuItemStartSimu);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuActions);

		menuBar.add(this.menuPanic);
		menuBar.add(menuAuthentification);

		JPanel panelButtons = new JPanel();

		// panelButtons.add(buttonFetchSymbols);
		// panelButtons.add(buttonFetchTicker); // Utile pour des test unitaire

		// panelButtons.add(buttonStartThreadThreading);

		panelGlobal.add(panelButtons, BorderLayout.NORTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panelGlobal, BorderLayout.CENTER);
		frame.getContentPane().add(labelLog, BorderLayout.SOUTH);
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

	private void setPanic(boolean isPanic, int nbPanic, int nbPanicNo) {
		Color colorBackGround = Color.GREEN;
		String text;
		if (isPanic) {
			text = "PANIC!";
			colorBackGround = Color.RED;
			beep();
		} else if (nbPanic == 0) {
			text = "NO PANIC! " + nbPanicNo;
			colorBackGround = Color.GREEN;
		} else {
			text = "NO PANIC now !" + nbPanic + " panics " + nbPanicNo + " ";
			colorBackGround = Color.ORANGE;
		}
		this.menuPanic.setBackground(colorBackGround);
		this.menuPanic.setText(text);

	}

	private void beep() {
		try {
			Toolkit.getDefaultToolkit().beep();
		} catch (Throwable e) {

		}
	}

	private void fetchTickers() {
		try {
			labelLog.setText("fetchTickers");
			BitfinexClient bitfinexClient = new BitfinexClient("", "");
			String devises = SymbolsConfig.getInstance().getSymbolsSelectedRequest();
			Object o = bitfinexClient.serviceProcess(EnumService.tickersV2, "", devises);

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void configureBitFinexKey() {
		this.labelLog.setText("Configure bitfinex keys ");
		try {
			String apiKey = this.dialogInputKey.getApiKey();
			String secretKey = this.dialogInputKey.getSecretKey();
			configFileProtected.set(ConfigFileProtected.keyBifinexApiKey, apiKey);
			configFileProtected.set(ConfigFileProtected.keyBitfinexSecretKey, secretKey);
		} catch (Exception e) {
			this.labelLog.setText("Exception " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void displayDialogInputKey() {
		this.dialogInputKey.setVisible(true);
	}

	private void fetchSymbols() {
		try {
			labelLog.setText("fetchSimbols");
			BitfinexClient bitfinexClient = new BitfinexClient("", "");
			Object o = bitfinexClient.serviceProcess(EnumService.symbols, "", "");
			Symbols symbols = (Symbols) o;
			List<String> listUsd = symbols.getAllEndWith("usd");
			for (String s : listUsd) {
				System.out.println("symbol :::: " + s);
			}
			System.err.println("listUsd.size : " + listUsd.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void selectCurrencyDialog() {
		this.dialogSelectSymbols.setVisible(true);
	}

	private void processSymbolsSelected() {
		System.out.println("Process Symbol Selected");

	}

	private void startThreads() {
		threadFetchTickers = new ThreadFetchTickers();
		threadProcessTickers = new ThreadProcessTickers();
		try {
			MidasGUI.this.configFileProtected = new ConfigFileProtected(Config.getInstance().getPassword());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startAuthenticatedThread();
	}

	public static MidasGUI getInstance() {
		return instance;
	}

	PanelCurrencies panelCurrencies;

	public void updateThread() {
		try {
			SessionCurrencies session = this.threadFetchTickers.getSesionCurrencies();
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					boolean isModePanic = session.isModePanic();
					int nbPanic = session.getAmbianceMarket().getNbPanic();
					int nbPanicNo = session.getAmbianceMarket().getNbPanicNo();
					setPanic(isModePanic, nbPanic, nbPanicNo);
				}
			};
			SwingUtilities.invokeLater(runnable);
			if (panelCurrencies == null) {
				panelCurrencies = new PanelCurrencies(session);
				panelGlobal.removeAll();
				panelGlobal.add(panelCurrencies);
				frame.pack();
			} else {
				panelCurrencies.update(session);
			}
			for (String name : hDetails.keySet()) {
				DialogShowCurrencyDetail dscd = hDetails.get(name);
				SessionCurrency sc = session.getSessionCurrency_byName(name);
				dscd.updateThread(sc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void emergencySave() {
		System.out.println("Emergency Save");
		this.threadFetchTickers.emergencySaveInDollar("Operator");
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
		if (this.panelCurrencies == null) {

		} else {
			this.panelCurrencies.updateAlgo(algoProcess);
		}
	}

	public void updateThreadBalance(Balances balances) {
		if (this.panelCurrencies == null) {
			System.err.println("PanelCurrencies is not yet initialized");
		} else {
			this.panelCurrencies.updateBalances(balances);
		}
	}

	private void startAuthenticatedThread() {
		if (this.threadBalance != null) {
			threadBalance.stop("GUI");
		}
		this.threadBalance = new ThreadBalance();
	}

	HashMap<String, DialogShowCurrencyDetail> hDetails = new HashMap<>();

	public void showDetail(SessionCurrency session, boolean b) {
		String name = session.getName();
		System.err.println("Show Detail " + name + "   " + b);
		DialogShowCurrencyDetail dialogShowCurrency = hDetails.get(name);
		if ((dialogShowCurrency == null) && b) {
			Point location = new Point(this.frame.getLocation().x + 10, this.frame.getLocation().y + 10);
			dialogShowCurrency = new DialogShowCurrencyDetail(session, location);
			hDetails.put(name, dialogShowCurrency);
		} else {
			dialogShowCurrency.setVisible(b);
		}
	}

	public void removeDetail(String name) {
		hDetails.remove(name);
	}

	private void removePanic() {
		System.err.println("remove PAnic");
		this.setPanic(false, 0, 0);
		SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
		sessionCurrencies.getAmbianceMarket().setNbPanic(0);
		sessionCurrencies.getAmbianceMarket().setNbPanicNo(0);
		log("Remove Panic");
	}

	private void startSimu() {
		new DialogSimuGUI();
	}

	public void log(String s) {
		SwingUtilities.invokeLater(() -> {
			labelLog.setText(s);
		});
	}

	public void log(List<Order> listOrdersAchat, List<Order> listOrdersVente) {
		String s = "Orders ";
		if (listOrdersAchat == null) {

		} else {
			s += "Orders Achat n :" + listOrdersAchat.size();
			for (Order o : listOrdersAchat) {
				s += " " + o.getCurrencyTo() + " " + o.getAmmount() + "  " + o.getCurrencyFrom() + " ;";
			}
		}
		if (listOrdersVente == null){
			
		}else {
			s += "Orders Vente n :" + listOrdersVente.size();
			for (Order o : listOrdersVente) {
				s += " " + o.getCurrencyTo() + " " + o.getAmmount() + "  " + o.getCurrencyFrom() + " ;";
			}
		}
		log(s);
	}

	private void testBestCurrency() {
		SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
		if (sessionCurrencies == null) {
			System.err.println("No sessionCurrencies for processing orders");
			log("No session yet . Just try again later");
			return;
		}
		SessionCurrency sessionBest = sessionCurrencies.getBestEligible();
		log("Best Currency : " + sessionBest);
	}
}
