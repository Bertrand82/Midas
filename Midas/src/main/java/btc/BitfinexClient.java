package btc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import btc.model.Symbols;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * Pour générer key : https://www.bitfinex.com/api
 * 
 * @author c82bgui
 * 
 */
public class BitfinexClient {
	
	public enum Params {
		No,
		Currency,
		Symbol,
		SymbolsGetParams;
	}
	public enum Type {
		
		Authenticated,
		Public,
		Authenticated_V2,
		Public_V2;

	}

	public enum EnumServiceV2 {
		status("status") ;
		boolean needSymbolInURL = false;
		boolean needCurrencyInURL= false	;	
		
		String key;
		EnumServiceV2(String key){
			this.key = key;
		}
	}
	public enum EnumService {
		balances("balances",Type.Authenticated ,true),
		account_infos("account_infos",Type.Authenticated ,true),
		summary("summary",Type.Authenticated ,true),
		deposit_new("deposit/new",Type.Authenticated ,true),
		key_info("key_info",Type.Authenticated ,true),
		margin_infos("margin_infos",Type.Authenticated ,true),
		transfer("transfer",Type.Authenticated ,true),
		withdraw("withdraw",Type.Authenticated ,true)	,
		symbols("symbols",Type.Public ,true,Symbols.class),
		symbols_details("symbols_details",Type.Public ,true),
		stats("stats",Type.Public ,true,null,Params.Symbol),
		ticker("pubticker",Type.Public ,true,null,Params.Symbol),
		orderbook("book",Type.Public ,true,null,Params.Symbol),
		trades("trades",Type.Public ,true,null,Params.Symbol),
		lends("lends",Type.Public ,true,null,Params.Currency),
		statusV2("platform/status",Type.Public_V2 ,true,null,Params.No) ,
		tickerV2("ticker",Type.Public_V2 ,true,null,Params.Symbol),
		tickersV2("tickers",Type.Public_V2 ,true,null,Params.SymbolsGetParams),
		tradesV2("trades",Type.Public_V2 ,true,null,Params.Symbol,"hist"),
		walletV2("auth/r/wallets",Type.Authenticated_V2 ,true,null,Params.No,null);
		/**
		 * Nom du service : permet de construire l'url
		 */
		public String key;
		/**
		 * Demande à être identifié (nonce + cryptage)
		 */
		Type type;
		/**
		 * Return un aray de json . Inutile
		 */
		boolean returnArray;
		/**
		 * Class qui peut être eventuellement instanciée à partir du json
		 */
		Class<?> clazz;
		/**
		 * 
		 */
		Params param;
		/**
		 * 
		 * 
		 */
		String suffix;
		
		EnumService(String key,Type isPrivate, boolean returnArray){
			this(key,isPrivate,returnArray,null);
		}
		EnumService(String key,Type isPrivate, boolean returnArray, Class<?> clazz){						
			this(key,isPrivate,returnArray,clazz,Params.No);
		}
		EnumService(String key,Type type, boolean returnArray, Class<?> clazz,Params param){	
			this(key,type,returnArray,clazz,param,null);
		}
		EnumService(String key,Type type, boolean returnArray, Class<?> clazz,Params param,String suffix){						
			this.key=key;
			this.type=type;
			this.clazz = clazz;
			this.param=param;
			this.suffix=suffix;
		}
		/**
		 * Instancie une classe a partir du json
		 * @param jo
		 * @return
		 * @throws Exception
		 */
		public Object instancie(JSONObject jo) throws Exception{
			if (jo == null){
				return null;
			}
			if (clazz == null){
				return jo;
			}
			Constructor<?> c = clazz.getConstructor(JSONObject.class);
			Object o = c.newInstance(jo);
			return o;
		}
	
		public boolean isPrivate() {
			// TODO Auto-generated method stub
			return ((type == Type.Authenticated) || (type == Type.Authenticated_V2));
		}
		public boolean isV1() {
			
			return  ((type == Type.Authenticated) || (type == Type.Public));
		}
		public String getVersion() {
			if (isV1()){
				return "v1";
			}else {
				return "v2";
			}
			
		}
		public String getKeyCanonique() {
			return key.replace('/', '_');
		}
	}

	
	private static final String ALGORITHM_HMACSHA384 = "HmacSHA384";

	
	private String apiKey = null;
	private String apiKeySecret = null;
	private long nonce = System.currentTimeMillis();

	/**
	 * public and authenticated access
	 *
	 * @param apiKey
	 * @param apiKeySecret
	 */
	public BitfinexClient(String apiKey, String apiKeySecret) {
		this.apiKey = apiKey;
		this.apiKeySecret = apiKeySecret;
	}

	
	

	public void serviceProcessV1(EnumService service) throws Exception {
		serviceProcess(service,null,null);
	}
	
		
	public void serviceProcess(EnumService service,String currency, String symbol) throws Exception {
		System.out.println("\nstart   ------------------------------------------------------------------------ "+service);
		String r;
		if(service.isPrivate()){
			r = this.sendRequestV1Authenticated(service);
		} else {
			r = this.sendRequest(service,currency, symbol)	;
		}
		System.out.println("sendRequestV1 public service :"+service.key+" result :" + r);
		traceResult(r, service);
	}
	


	private void traceResult(String result,EnumService service){

		try {
			String joStr;
			if (result.startsWith("{")) {
				joStr = result;
			} else {
				joStr = "{" + service.getKeyCanonique() + ":" + result + "}";
			}
			System.out.println(joStr);
			JSONObject jo = new JSONObject(joStr);
			Object object = service.instancie(jo);
			System.out.println("sendRequestV1 "+service.key+" result json :" + jo);
			List<Object> list = new ArrayList<Object>();
			if (jo.has(service.key)) {
				JSONArray array = jo.getJSONArray(service.key);
				for (int i = 0; i < array.length(); i++) {					
					System.out.println(service.key+" ----->   "+ array.get(i));
					list.add( array.get(i));
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String sendRequest(EnumService service,String currency, String symbol) throws Exception{
		String sResponse;

		HttpURLConnection conn = null;
		String urlPath = "/"+service.getVersion()+"/" + service.key;
		if (service.param == Params.No ){
		}else if (service.param == Params.Currency ){
					if(currency == null || currency.length()==0){
				throw new Exception("need Currency for "+service.name());
			}
			urlPath+="/"+currency;
		}else if(service.param == Params.Symbol ){
			if(symbol == null || symbol.length()==0){
				throw new Exception("Need Symbol for "+service.name());
			}
			urlPath+="/"+symbol;
		}else if(service.param == Params.SymbolsGetParams ){
			if(symbol == null || symbol.length()==0){
				throw new Exception("Need Symbol for "+service.name());
			}
			urlPath+="?symbols="+symbol;
		}
		if(isonsistent(service.suffix)){
			urlPath+="/"+service.suffix;
		}
		String method = "GET";
		

		try {
			URL url = new URL("https://api.bitfinex.com" + urlPath);
			System.out.println("url "+url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			// read the response
			InputStream in = new BufferedInputStream(conn.getInputStream());
			return convertStreamToString(in);

		} catch (IOException e) {
			System.out.println("IoException :::: "+e.getMessage());
			String errMsg = e.getLocalizedMessage();

			if (conn != null) {
				try {
					sResponse = convertStreamToString(conn.getErrorStream());
					errMsg += " -> " + sResponse;
					Log( errMsg, e);
					return sResponse;
				} catch (IOException e1) {
					errMsg += " Error on reading error-stream. -->: " + e1.getLocalizedMessage();
					Log( errMsg, e);
					throw new IOException(e.getClass().getName(), e1);
				}
			} else {
				throw new IOException(e.getClass().getName(), e);
			}
		}  finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private boolean isonsistent(String s) {
		if(s == null){
			return false;
		}
		if(s.length()== 0){
			return false;
		}
		return true;
	}

	/**
	 * Creates an authenticated request WITHOUT request parameters. Send a
	 * request for Balances.
	 *
	 * @return Response string if request successfull
	 * @throws IOException
	 */
	public String sendRequestV1Authenticated(EnumService service_) throws IOException {
		String sResponse;

		HttpURLConnection conn = null;

		// String urlPath = "/v1/balances";
		String urlPath = "/"+service_.getVersion()+"/"+ service_.key;
		// String method = "GET";
		String method = "POST";

		try {
			URL url = new URL("https://api.bitfinex.com" + urlPath);
			System.out.println("url "+url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);

			conn.setDoOutput(true);
			conn.setDoInput(true);

			JSONObject jo = new JSONObject();
			jo.put("request", urlPath);
			jo.put("nonce", Long.toString(getNonce()));

			// API v1
			String payload = jo.toString();

			// this is usage for Base64 Implementation in Android. For pure java
			// you can use java.util.Base64.Encoder
			// Base64.NO_WRAP: Base64-string have to be as one line string
			// String payload_base64 = Base64.encodeToString(payload.getBytes(),
			// Base64.NO_WRAP);
			String payload_base64 = Base64.getEncoder().encodeToString(payload.getBytes());

			System.out.println("payload :" + payload);
			String payload_sha384hmac = hmacDigest(payload_base64, apiKeySecret, ALGORITHM_HMACSHA384);

			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.addRequestProperty("X-BFX-APIKEY", apiKey);
			conn.addRequestProperty("X-BFX-PAYLOAD", payload_base64);
			conn.addRequestProperty("X-BFX-SIGNATURE", payload_sha384hmac);

			// read the response
			InputStream in = new BufferedInputStream(conn.getInputStream());
			return convertStreamToString(in);

		} catch (MalformedURLException e) {
			throw new IOException(e.getClass().getName(), e);
		} catch (ProtocolException e) {
			throw new IOException(e.getClass().getName(), e);
		} catch (IOException e) {
			System.out.println("IoException :::: "+e.getMessage());
			String errMsg = e.getLocalizedMessage();

			if (conn != null) {
				try {
					sResponse = convertStreamToString(conn.getErrorStream());
					errMsg += " -> " + sResponse;
					Log( errMsg, e);
					return sResponse;
				} catch (IOException e1) {
					errMsg += " Error on reading error-stream. -->: " + e1.getLocalizedMessage();
					Log( errMsg, e);
					throw new IOException(e.getClass().getName(), e1);
				}
			} else {
				throw new IOException(e.getClass().getName(), e);
			}
		} catch (JSONException e) {
			String msg = "Error on setting up the connection to server";
			throw new IOException(msg, e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private String convertStreamToString(InputStream is) throws IOException {
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

	public long getNonce() {
		return ++nonce;
	}

	public boolean isAccessPublicOnly() {
		return apiKey == null;
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
		} catch (UnsupportedEncodingException e) {
			Log("Exception: " + e.getMessage());
		} catch (InvalidKeyException e) {
			Log( "Exception: " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			Log( "Exception: " + e.getMessage());
		}
		System.out.println("digest : " + digest);
		return digest;
	}

	
	static void Log(Object... strings) {
		String s = "";
		for (Object ss : strings) {
			s += ss + " : ";
		}
		System.out.println(s);
	}
	

}