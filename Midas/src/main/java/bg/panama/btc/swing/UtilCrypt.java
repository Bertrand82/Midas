package bg.panama.btc.swing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class UtilCrypt {
	
	public static String keyBifinexApiKey ="kbfapik1";
	public static String keyBitfinexSecretKey ="kbfsk2";
	Properties properties = new Properties();
	static final File dirPrivate = new File("private");
	static final File defaultFile = new File(dirPrivate,"p_config_private.properties");
	
	private  char[] password;

    // Decreasing this speeds down startup time and can be useful during testing, but it also makes it easier for brute force attackers
	private   int iterationCount = 40000;
	
	 // Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
    private int keyLength = 128;
  
	private  byte[] salt = new String("1234678").getBytes();
	
    
	 
	public UtilCrypt(String password_){
		
		if (password_ == null){
			password_ ="";
		}
		this.password = password_.toCharArray();		
	
	}

   

    public  SecretKeySpec createSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public  String encrypt(String property) throws Exception {
    	SecretKeySpec key = createSecretKey();
    	Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private  String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public  String decrypt(String string ) throws Exception {
    	SecretKeySpec key = createSecretKey();
    	String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

   

    private  byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }



	
	
	
}