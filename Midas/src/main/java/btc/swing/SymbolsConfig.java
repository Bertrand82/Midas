package btc.swing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class SymbolsConfig {
	
	public static Object[][] data = { { "btc", "bitecoin", new Boolean(false), " " }, { "ltc",new Boolean(false), " " },
			{ "eth",new Boolean(false), " " }, { "etc",new Boolean(false), " " }, { "rrt",new Boolean(false), " " },
			{ "zec",new Boolean(false), " " }, { "xmr",new Boolean(false), " " }, { "dsh",new Boolean(false), " " },
			{ "bcc",new Boolean(false), " " }, { "bcu",new Boolean(false), " " }, { "xrp",new Boolean(false), " " },
			{ "iot",new Boolean(false), " " }, { "eos",new Boolean(false), " " }, { "san",new Boolean(false), " " },
			{ "omg",new Boolean(false), " " }, { "bch",new Boolean(false), " " }, { "neo",new Boolean(false), " " },
			{ "etp",new Boolean(false), " " }, { "qtm",new Boolean(false), " " }, { "bt1",new Boolean(false), " " },
			{ "bt2",new Boolean(false), " " }, { "avt",new Boolean(false), " " }, { "edo",new Boolean(false), " " },
			{ "btg",new Boolean(false), " " }, { "dat",new Boolean(false), " " }, { "qsh",new Boolean(false), " " },
			{ "yyw",new Boolean(false), " " }, };


	private static SymbolsConfig instance;
	static {
		try {
			instance = new SymbolsConfig();
			instance.loadFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private File file = new File("symbols.properties");
	private List< SymbolConfig> lSymbols = new ArrayList< SymbolConfig>();

	public static SymbolsConfig getInstance() {

		return instance;
	}

	public void store()  {
		Properties p = toProperties();
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			p.store(writer, "maj symbols");
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void loadFromFile()  {
		Properties p = new Properties();
		if (file.exists()) {
			FileReader reader = null;
			try {
				reader = new FileReader(file);
				p.load(reader);
				for (Object key : p.keySet()) {
					String v = p.getProperty("" + key);
					SymbolConfig s = new SymbolConfig(v);
					this.lSymbols.add(s);
				}
				Collections.sort(lSymbols);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
           for(Object[] oArray : data){
        	   String s="";
        	   for(Object oo : oArray){
        		  s += oo+";";
        	   }
        	   SymbolConfig sc = new SymbolConfig(s);
        	   System.out.println("Symbols   ----- "+sc);
        	   lSymbols.add( sc);
           }
           Collections.sort(lSymbols);
		}
	}

	Properties toProperties() {
		Properties p = new Properties();
		for (SymbolConfig symbol : this.lSymbols) {
			p.setProperty(symbol.getName(), symbol.toString());
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
	
	List<SymbolConfig> getListSymbolsSelected(){
		List<SymbolConfig> listSelected = new ArrayList<SymbolConfig>();
		for(SymbolConfig sy : this.lSymbols){
			if (sy.isSelected()){
				listSelected.add(sy);
			}
		}
		return listSelected;		
	}
	/**
	 * Par exemple : "tBTCUSD"
	 * ou "tBTCUSD,tLTCUSD";
	 * @return
	 */
	public String getSymbolsSelectedRequest() {
		List<SymbolConfig> list = this.getListSymbolsSelected();
		String s ="";
		String separator="";
		for(SymbolConfig sy : list){
			s+= separator+"t"+sy.getName().toUpperCase()+"USD";
			separator=",";
		}
		return s;
	}
}
