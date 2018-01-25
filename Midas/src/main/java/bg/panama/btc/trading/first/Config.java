package bg.panama.btc.trading.first;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.Properties;


import bg.panama.btc.swing.UtilCrypt;

public class Config implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String KEY_SAVE_PASSWORD = "SavePassword";
	private static final String KEY_PASSWORD = "Password";
	private static final String KEY_orderAble = "isOrderAble";
	private static final String KEY_orderAbleAchat = "isOrderAbleAchat";
	private static final String KEY_orderAbleVente = "isOrderAbleVente";
	private static final String KEY_plafondCryptoInDollar = "plafondCryptoInDollar";

	private boolean orderAble = false;
	private boolean orderAbleAchat = false;
	private boolean orderAbleVente = true;
	private boolean savePassword;
	private int plafondCryptoInDollar =1000;
	private transient String password = null;
	private Properties pConfig = new Properties();
	private File fileConfig = new File("p_config_gui.properties");
	private UtilCrypt utilCrypt = new UtilCrypt("bitfinex");
	
	private static Config instance = new Config(false, "");

	private Config(boolean orderAble, String password) {
		super();
		this.orderAble = orderAble;
		this.password = password;
		instance = this;
		readConfigFile();
	}

	public boolean isOrderAble() {
		return orderAble;
	}

	public void setOrderAble(boolean orderAble) {
		this.orderAble = orderAble;
		this.saveConfigFile();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.saveConfigFile();
	}

	public static Config getInstance() {
		return instance;
	}

	private void readConfigFile() {
		try {
			System.out.println("ReadConfigFile");
			if (fileConfig.exists()) {
				FileReader reader = new FileReader(fileConfig);
				pConfig.load(reader);
				reader.close();
			}
			savePassword = pConfig.getProperty(KEY_SAVE_PASSWORD, "").equalsIgnoreCase("true");			
			orderAble = pConfig.getProperty(KEY_orderAble, "").equalsIgnoreCase("true");
			orderAbleAchat = pConfig.getProperty(KEY_orderAbleAchat, "").equalsIgnoreCase("true");
			orderAbleVente = pConfig.getProperty(KEY_orderAbleVente, "").equalsIgnoreCase("true");
			String plafondCryptoInDollarStr=pConfig.getProperty(KEY_plafondCryptoInDollar, "1000");
			plafondCryptoInDollar=Integer.parseInt(plafondCryptoInDollarStr);
			proceessPasswordSaved();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveConfigFile() {
		System.out.println("SaveConfigFile");
		try {
			if (savePassword) {
				pConfig.setProperty(KEY_PASSWORD, getPasswordcrypted());
			} else {
				pConfig.setProperty(KEY_PASSWORD, "");
			}
			pConfig.setProperty(KEY_orderAble, "" + isOrderAble());
			pConfig.setProperty(KEY_orderAbleAchat, "" + isOrderAbleAchat());
			pConfig.setProperty(KEY_orderAbleVente, "" + isOrderAbleVente());
			pConfig.setProperty(KEY_SAVE_PASSWORD, "" + savePassword);
			pConfig.setProperty(KEY_plafondCryptoInDollar, "" + plafondCryptoInDollar);
			
			FileWriter writer = new FileWriter(fileConfig);

			pConfig.store(writer, "Fichier de cobfig MidasGUI");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void proceessPasswordSaved() {
		try {
			String passwordCrypted = this.pConfig.getProperty(KEY_PASSWORD);
			this.password = getPasswordDecrypted(passwordCrypted);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getPasswordcrypted() {
		try {
			return utilCrypt.encrypt(this.password);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String getPasswordDecrypted(String s) {
		try {
			return utilCrypt.decrypt(s);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	
	public void setSavePassword(boolean savePassword) {
		this.savePassword= savePassword;
		saveConfigFile();
	}
	
	public  boolean getSavePassword() {
		return this.savePassword;
	}

	public boolean isOrderAbleAchat() {
		return orderAbleAchat;
	}

	public void setOrderAbleAchat(boolean orderAbleAchat) {
		this.orderAbleAchat = orderAbleAchat;
		saveConfigFile();
	}

	public boolean isOrderAbleVente() {
		return orderAbleVente;
	}

	public void setOrderAbleVente(boolean orderAbleVente) {
		this.orderAbleVente = orderAbleVente;
		saveConfigFile();
	}

	public int getPlafondCryptoInDollar() {
		return plafondCryptoInDollar;
	}

	public void setPlafondCryptoInDollar(int plafondCryptoInDollar) {
		this.plafondCryptoInDollar = plafondCryptoInDollar;
		saveConfigFile();
	}

}
