package bg.panama.btc;

import java.io.File;

import bg.panama.btc.swing.ConfigFileProtected;

public class BitfinexClientFactory {

	
	public static BitfinexClient getBitfinexClient(String password){
		File defaultFile = new File("p_configTest.properties");
		if (!defaultFile.exists()){
			System.err.println("Pas de fichier de config de test!!!!!");
			throw new RuntimeException("PAs de fichier de config crypte pour keys");
		}
		BitfinexClient bfnx = null;
		 try {
			 ConfigFileProtected pcf  = new ConfigFileProtected(password);
			 System.out.println("getBitfinexClient "+pcf);
			 bfnx = getBitfinexClient(pcf);			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		 return bfnx;
		
	}
	
	public static BitfinexClient getBitfinexClientFree() {
		return new BitfinexClient();
	}
	
	public static BitfinexClient getBitfinexClientAuthenticated() {
		try {
			ConfigFileProtected pcf  = ConfigFileProtected.getInstance();
			
			return getBitfinexClient(pcf);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public static BitfinexClient getBitfinexClient(ConfigFileProtected pcf) throws Exception {
		 BitfinexClient bfnx;
		 String keyApi = pcf.get(ConfigFileProtected.keyBifinexApiKey);
		 String keySecret = pcf.get(ConfigFileProtected.keyBitfinexSecretKey);
		 bfnx = new BitfinexClient(keyApi, keySecret);
		 System.err.println("Key API :"+keyApi);
		 return bfnx;
	}
	
}
