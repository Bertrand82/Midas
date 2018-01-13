package btc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.swing.ConfigFileProtected;

public class AuthentificationV2Test {

	static final File defaultFile = new File("p_configTest.properties");
	String password = "mypassword";
	
	ConfigFileProtected pcf  ;
	String keyApi ;
	String keySecret ;
	private static final String ALGORITHM_HMACSHA384 = "HmacSHA384";

	public AuthentificationV2Test(){
		if (!defaultFile.exists()){
			System.err.println("Pas de fichier de config de test!!!!!");
			throw new RuntimeException("PAs de fichier de config crypte pour keys");
		}
		
		 try {
			 pcf  = new ConfigFileProtected(password,defaultFile);
			 keyApi = pcf.get(ConfigFileProtected.keyBifinexApiKey);
			 keySecret = pcf.get(ConfigFileProtected.keyBitfinexSecretKey);
			 
			 System.err.println("Key API :"+keyApi);	 System.err.println("Key API :"+keyApi);
			 System.err.println("Key Secret :"+keySecret);	 System.err.println("Key API :"+keyApi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/*
	 
	 const crypto = require('crypto')
const request = require('request')

const apiKey = '<Your API key here>'
const apiSecret = '<Your API secret here>'

const apiPath = 'v2/auth/r/alerts'
const nonce = Date.now().toString()
const body = { "type": "price" }
const rawBody = JSON.stringify(body)
let signature = `/api/${apiPath}${nonce}${rawBody}`

signature = crypto
  .createHmac('sha384', apiSecret)
  .update(signature)
  .digest('hex')

const options = {
  url: `https://api.bitfinex.com/${apiPath}`,
  headers: {
    'bfx-nonce': nonce,
    'bfx-apikey': apiKey,
    'bfx-signature': signature
  },
  body: body,
  json: true
}
request.post(options, (error, response, body) => {
  console.log(body);
})
	 
	 
	 r/wallets
	 */
	@Test
	public void  sendRequestV2Authenticated() {
		String sResponse;

		HttpURLConnection conn = null;
		//v2/auth/r/alerts
		
		String urlPath = "v2/auth/r/wallets";
		String method = "POST";

		try {
			URL url = new URL("https://api.bitfinex.com/" + urlPath);
			String nonce = Long.toString(getNonce());
			System.out.println("url "+url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);

			conn.setDoOutput(true);
			conn.setDoInput(true);

			JSONObject jo = new JSONObject();
			
			//jo.put("type", "price");
			
			
			// API v2
			String body = jo.toString();
            System.out.println(body);
			// this is usage for Base64 Implementation in Android. For pure java
			// you can use java.util.Base64.Encoder
			// Base64.NO_WRAP: Base64-string have to be as one line string
			// String payload_base64 = Base64.encodeToString(payload.getBytes(),
			// Base64.NO_WRAP);
            String signature = "/api/" + urlPath + nonce + body;
            System.out.println("signature1 "+signature);
			String signature_base64 = Base64.getEncoder().encodeToString(signature.getBytes());

			System.out.println("signature :" + signature);
			String signature_sha384hmac = hmacDigest(signature_base64, keySecret, ALGORITHM_HMACSHA384);
			String signature_sha384hmac_ = hmacDigest(signature, keySecret, ALGORITHM_HMACSHA384);

			conn.setRequestProperty("content-type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			 
			    
			  
			conn.addRequestProperty("bfx-apikey", keyApi);
			conn.addRequestProperty("bfx-nonce", nonce);
			conn.addRequestProperty("bfx-signature", signature_sha384hmac_);
			JSONObject jo2 = new JSONObject();
			//jo2.put("type", "price");
			jo2.put("json", "true");
			String postData = jo2.toString();
			System.out.println("postDAta "+postData);
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");

	        conn.getOutputStream().write(postDataBytes);
			// read the response
			InputStream in = new BufferedInputStream(conn.getInputStream());
			String s = convertStreamToString(in);

		} catch (Exception e) {
			System.out.println("Exception333 :::: "+e.getMessage());
			e.printStackTrace();
			
			if (conn != null) {
				try {
					sResponse = convertStreamToString(conn.getErrorStream());
					System.err.println("sResponse "+sResponse);
				} catch (Exception e1) {
				}
			} 
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}


	


	private long getNonce() {
		
		return System.currentTimeMillis();
	}
	private static String convertStreamToString(InputStream is) throws IOException {
		if (is == null) {
			System.out.println("convertStreamToString stream is null");
			return null;
		}
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(ir);
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public static String hmacDigest(String msg, String keyString, String algo) {
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);

			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("digest : " + digest);
		return digest;
	}


}
