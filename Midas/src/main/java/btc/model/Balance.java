package btc.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Balance {

	
	public static String TYPE_exchange ="exchange";
	public static String TYPE_deposit ="deposit";
	String type;
	String currency;
	double amount;
	double available ;
	// {"amount":"0.0","available":"0.0","currency":"btc","type":"deposit"}
	public Balance(JSONObject jsonO) throws Exception{
		
		this.amount= jsonO.getDouble("amount");
		this.available = jsonO.getDouble("available");
		this.currency =jsonO.getString("currency");
		this.type = jsonO.getString("type");
	}
	public String getType() {
		return type;
	}
	public String getCurrency() {
		return currency;
	}
	public double getAmount() {
		return amount;
	}
	public double getAvailable() {
		return available;
	}
	
	public boolean isTypeEchange(){
		return this.type.equalsIgnoreCase(TYPE_exchange);
	}
	@Override
	public String toString() {
		return "Balance " + type + " " + currency + " " + amount + " " + available
				+ "]";
	}

}
