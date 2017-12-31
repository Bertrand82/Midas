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
	
	double lastPrice ;
	double percent ;
	double availableInDollar;
	
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
	public double getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public double getAvailableInDollar() {
		return availableInDollar;
	}
	public void setAvailableInDollar(double availableInDollar) {
		this.availableInDollar = availableInDollar;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void setAvailable(double available) {
		this.available = available;
	}

}
