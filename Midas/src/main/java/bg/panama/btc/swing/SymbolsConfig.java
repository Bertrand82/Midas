package bg.panama.btc.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SymbolsConfig {

	public static Object[][] data = { { "btc", "bitecoin", new Boolean(false), " " },
			{ "ltc", new Boolean(false), " " }, { "eth", new Boolean(false), " " }, { "etc", new Boolean(false), " " },
			{ "rrt", new Boolean(false), " " }, { "zec", new Boolean(false), " " }, { "xmr", new Boolean(false), " " },
			{ "dsh", new Boolean(false), " " }, { "bcc", new Boolean(false), " " }, { "bcu", new Boolean(false), " " },
			{ "xrp", new Boolean(false), " " }, { "iot", new Boolean(false), " " }, { "eos", new Boolean(false), " " },
			{ "san", new Boolean(false), " " }, { "omg", new Boolean(false), " " }, { "bch", new Boolean(false), " " },
			{ "neo", new Boolean(false), " " }, { "etp", new Boolean(false), " " }, { "qtm", new Boolean(false), " " },
			{ "bt1", new Boolean(false), " " }, { "bt2", new Boolean(false), " " }, { "avt", new Boolean(false), " " },
			{ "edo", new Boolean(false), " " }, { "btg", new Boolean(false), " " }, { "dat", new Boolean(false), " " },
			{ "qsh", new Boolean(false), " " }, { "yyw", new Boolean(false), " " }, };

	private static SymbolsConfig instance;
	static {
		try {
			instance = new SymbolsConfig();
			instance.loadFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private File file_Symbol_selected = new File("p_symbolsSelected.properties");
	private File file_Symbol_comment = new File("p_symbolsComments.properties");
	private File file_Symbol_sort = new File("p_symbolsSort.properties");
	private File file_Symbol_max_trade = new File("p_symbolsMaxTrade.properties");
	private File file_Symbol_indicator_panic = new File("p_symbolsIndicatorPanic.properties");
	private List<SymbolConfig> lSymbols = new ArrayList<SymbolConfig>();

	public static SymbolsConfig getInstance() {
		return instance;
	}

	public void store() {
		storeSelected();
		storeMaxTrade();
		storeIndicatorPanic();
	}

	private void storeMaxTrade() {
		Properties p = toPropertiesMaxTrade();
		store(p, file_Symbol_max_trade, "Mis a jour max trade");
	}

	public void storeIndicatorPanic() {
		Properties p = toPropertiesIndicatorPanic();
		store(p, file_Symbol_indicator_panic, "Mis a jour indicator Panic");
	}

	private void store(Properties p, File f, String comment) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(f);
			p.store(writer, comment);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void storeSelected() {
		Properties p = toPropertiesIsSelected();
		store(p, file_Symbol_selected, "maj symbols");
	}

	public void loadFromFile() {
		Properties prpSymbol_Selected = loadPropertiesSymbolSelected();
		Properties prpSymbol_Currency_ = loadPropertiesSymbolCurrencies();
		Properties prpSymbol_Sort = loadPropertiesSymbolSort();
		Properties prpSymbol_MaxTrade = loadPropertiesSymbolMaxTrade();
		Properties prpIndicatorPanic = loadPropertiesIndicatorPanic();

		if (file_Symbol_selected.exists()) {
			FileReader reader = null;
			try {
				reader = new FileReader(file_Symbol_selected);
				prpSymbol_Selected.load(reader);
				for (Object key : prpSymbol_Selected.keySet()) {
					String v = prpSymbol_Selected.getProperty("" + key);
					SymbolConfig sym = new SymbolConfig("" + key, v);
					sym.setComment(prpSymbol_Currency_.getProperty("" + key, ""));
					sym.setOrder(prpSymbol_Sort.getProperty("" + key, "10000"));
					sym.setMaxTrade(prpSymbol_MaxTrade.getProperty("" + key, "0"));
					sym.setIndicatorPanic(prpIndicatorPanic.getProperty("" + key, "true"));
					this.lSymbols.add(sym);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			int i = 0;
			for (Object[] oArray : data) {
				String s = "";
				s = ("" + oArray[0]).trim();
				SymbolConfig sc = new SymbolConfig(s, "false");
				sc.setComment(prpSymbol_Currency_.getProperty(s));
				sc.setOrder(prpSymbol_Sort.getProperty(s, "10000"));
				System.out.println("Symbols   ----- s:" + s + " xxx  " + sc);
				lSymbols.add(sc);
			}

		}
		Collections.sort(lSymbols);
	}

	private Properties loadPropertiesSymbolCurrencies() {
		Properties p = new Properties();
		load(p, file_Symbol_comment);
		return p;
	}

	private Properties loadPropertiesSymbolSort() {
		Properties p = new Properties();
		load(p, file_Symbol_sort);
		return p;
	}

	private Properties loadPropertiesSymbolSelected() {
		Properties p = new Properties();
		load(p, file_Symbol_selected);
		return p;
	}

	private Properties loadPropertiesSymbolMaxTrade() {
		Properties p = new Properties();
		load(p, file_Symbol_max_trade);
		return p;
	}
	
	private Properties loadPropertiesIndicatorPanic() {
		Properties p = new Properties();
		load(p, file_Symbol_indicator_panic);
		return p;
	}


	public static void load(Properties p, File file) {
		try {
			if (file.exists()) {
				FileInputStream inStream = new FileInputStream(file);
				p.load(inStream);
				inStream.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Properties toPropertiesIsSelected() {
		Properties p = new Properties();
		for (SymbolConfig symbol : this.lSymbols) {
			p.setProperty(symbol.getName(), "" + symbol.isSelected());
		}
		return p;
	}

	Properties toPropertiesMaxTrade() {
		Properties p = new Properties();
		for (SymbolConfig symbol : this.lSymbols) {
			p.setProperty(symbol.getName(), "" + symbol.getMaxTrade());
		}
		return p;
	}
	Properties toPropertiesIndicatorPanic() {
		Properties p = new Properties();
		for (SymbolConfig symbol : this.lSymbols) {
			p.setProperty(symbol.getName(), "" + symbol.isIndicatorPanic());
		}
		return p;
	}

	public List<SymbolConfig> gethSymbols() {
		return lSymbols;
	}

	public SymbolConfig getSymbolConfig(int row) {
		// TODO Auto-generated method stub
		return lSymbols.get(row);
	}

	public SymbolConfig getSymbolConfig(String currency) {
		for (SymbolConfig sy : this.lSymbols) {
			if (sy.getName().equalsIgnoreCase(currency)) {
				return sy;
			}
		}
		System.err.println("ERROR4562 NO Symbol for currency "+currency);
		return null;
	}

	List<SymbolConfig> getListSymbolsSelected() {
		List<SymbolConfig> listSelected = new ArrayList<SymbolConfig>();
		for (SymbolConfig sy : this.lSymbols) {
			if (sy.isSelected()) {
				listSelected.add(sy);
			}
		}
		return listSelected;
	}

	/**
	 * Par exemple : "tBTCUSD" ou "tBTCUSD,tLTCUSD";
	 * 
	 * @return
	 */
	public String getSymbolsSelectedRequest() {
		List<SymbolConfig> list = this.getListSymbolsSelected();
		String s = "";
		String separator = "";
		for (SymbolConfig sy : list) {
			s += separator + "t" + sy.getName().toUpperCase() + "USD";
			separator = ",";
		}
		return s;
	}
	
	

}
