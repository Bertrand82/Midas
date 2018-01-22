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

public class ConfigFileProtected {
	
	public static String keyBifinexApiKey ="kbfapik1";
	public static String keyBitfinexSecretKey ="kbfsk2";
	Properties properties = new Properties();
	static final File dirPrivate = new File("private");
	static final File defaultFile = new File(dirPrivate,"p_config_private.properties");
	File file ;
	private static ConfigFileProtected instance;
	
	private  char[] password;

    // Decreasing this speeds down startup time and can be useful during testing, but it also makes it easier for brute force attackers
	private   int iterationCount = 40000;
	
	 // Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
    private int keyLength = 128;
  
	private  byte[] salt = new String("12345678").getBytes();
	

	 
	public ConfigFileProtected(String password) throws Exception{
		this(password,defaultFile);
	}
	public ConfigFileProtected(String password,File file) throws Exception{
		if (password == null){
			password ="";
		}
		this.password = password.toCharArray();
		this.file = file;
		if (file.exists()){
			properties.load(new FileReader(file));
		}
		instance = this;
	}

   

    public  SecretKeySpec createSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public  String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
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

    protected  String decrypt(String string ) throws Exception {
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



	public void set(String key1, String value) throws Exception{
		String crypted = this.encrypt(value);
		this.properties.setProperty(key1, crypted);
		Writer w = null;
		try {
			 dirPrivate.mkdirs();
			 w = new FileWriter(file);
			this.properties.store(w, "store "+key1);
		} finally {
			w.close();
		} 
	}



	public String get(String key) throws Exception{
		String crypted = this.properties.getProperty(key);
		String clear = this.decrypt(crypted);
		return clear;
	}
	public static ConfigFileProtected getInstance() {
		return instance;
	}
	public static void removeInstance() {
		instance = null;
	}
	
	
}