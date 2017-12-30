package btc;

import java.io.File;

import btc.swing.ProtectedConfigFile;

public class BitfinexClientFactory {

	
	public static BitfinexClient getBitfinexClient(String password){
		File defaultFile = new File("p_configTest.properties");
		if (!defaultFile.exists()){
			System.err.println("Pas de fichier de config de test!!!!!");
			throw new RuntimeException("PAs de fichier de config crypte pour keys");
		}
		BitfinexClient bfnx;
		 try {
			 ProtectedConfigFile pcf  = new ProtectedConfigFile(password);
			 String keyApi = pcf.get(ProtectedConfigFile.keyBifinexApiKey);
			 String keySecret = pcf.get(ProtectedConfigFile.keyBitfinexSecretKey);
			 bfnx = new BitfinexClient(keyApi, keySecret);
			 System.err.println("Key API :"+keyApi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		 return bfnx;
		
	}
	
}
