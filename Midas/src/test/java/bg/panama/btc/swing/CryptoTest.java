package bg.panama.btc.swing;

import static org.junit.Assert.assertEquals;

import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import bg.panama.btc.swing.ConfigFileProtected;

public class CryptoTest {

	
	
	@Test
	 public  void processTest() throws Exception {
			String key1="keyTest";
			String value1 = "valueTest";
	       
		    ConfigFileProtected protectedConfigFile = new ConfigFileProtected("bertrand");
	       
	        SecretKeySpec key = protectedConfigFile.createSecretKey();

	        String originalPassword = "secret222";
	        System.out.println("Original password: " + originalPassword);
	        String encryptedPassword = protectedConfigFile.encrypt(originalPassword);
	        System.out.println("Encrypted password: " + encryptedPassword);
	        String decryptedPassword = protectedConfigFile.decrypt(encryptedPassword);
	        System.out.println("Decrypted password: " + decryptedPassword);
	        System.out.println("Original password: " + originalPassword);
	        assertEquals(decryptedPassword,originalPassword);
	        
	        protectedConfigFile.set(key1,value1);
	        String value2 = protectedConfigFile.get(key1);
	        protectedConfigFile = new ConfigFileProtected("bertrand");
	        String value3 = protectedConfigFile.get(key1);
	        assertEquals(value1,value2);
	        assertEquals(value1,value3);
	    }

}
