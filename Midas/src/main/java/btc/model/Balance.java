package btc.model;

import java.io.Serializable;

import org.json.JSONObject;

import btc.swing.SymbolConfig;
import btc.swing.SymbolsConfig;

public class Balance implements Serializable{

	private static final long serialVersionUID = 1L;
	public static String TYPE_exchange ="exchange";
	public static String TYPE_deposit ="deposit";
	String type;
	String currency;
	double amount;
	double available ;
	
	double lastPrice ;
	double percentHourlyByDay ;
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
	public double getPercentHourlyByDay() {
		return percentHourlyByDay;
	}
	public void setPercentHourlyByDay(double percent) {
		this.percentHourlyByDay = percent;
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
	public boolean isOverLimit() {
		SymbolConfig symbol = SymbolsConfig.getInstance().getSymbolConfig(currency);
		int maxDollar = symbol.getMaxTrade();
		if (maxDollar == 0){
			return false;
		}else {
			return ((getAvailableInDollar()+SEUIL_DOLLAR) > maxDollar);
		}
	}
	
	private static double SEUIL_DOLLAR = 50;
	/**
	 * Attention! 0 pas de limite
	 * @return
	 */
	public double getAchatMax() {
		SymbolConfig symbol = SymbolsConfig.getInstance().getSymbolConfig(currency);
		int maxDollar = symbol.getMaxTrade();
		if (maxDollar <= 0){
			return 0;
		}else {
			double maxPossibleInDollard = maxDollar -getAvailableInDollar();
			if (maxPossibleInDollard <SEUIL_DOLLAR){// j'ecrete .
				maxPossibleInDollard=0;
			}
			double max = maxPossibleInDollard/lastPrice;
			return  max;
		}
	}

}
