package bg.panama.btc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.json.JSONObject;

import bg.panama.btc.swing.SymbolConfig;
import bg.panama.btc.swing.SymbolsConfig;
import bg.panama.btc.trading.first.ServiceCurrencies;
@Entity
public class Balance implements Serializable{

	private static final long serialVersionUID = 1L;
	public static String TYPE_exchange ="exchange";
	public static String TYPE_deposit ="deposit";
	
	@Id
	@GeneratedValue
	private long id;


	private String type;
	private String currency;
	private double amount;
	private double available ;
	
	private double lastPrice ;
	private double percentHourlyByDay ;
	private double availableInDollar;
	private double amountInDollar;
	private Date date = new Date();
	@ManyToOne
	@JoinColumn(name="balances_id", nullable=false)
	private  Balances balances;
	// {"amount":"0.0","available":"0.0","currency":"btc","type":"deposit"}
	public Balance() {
		
	}
	public Balance(JSONObject jsonO, Balances balances) throws Exception{
		this.balances= balances;
		this.amount= jsonO.getDouble("amount");
		this.available = jsonO.getDouble("available");
		
		this.currency =jsonO.getString("currency");
		this.type = jsonO.getString("type");
		double lastPriceDollar = ServiceCurrencies.getInstance().getPriceInDollar(currency);
		this.availableInDollar = this.available * lastPriceDollar;
		this.amountInDollar = this.amount * lastPriceDollar;
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
		return "Balance " + type + " " + currency + "|amount :" + amount + "|avalaible :" + available+" | availableInDollar:"+availableInDollar
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
	
	
	public double getAmountInDollar() {
		return amountInDollar;
	}


	public void setAmountInDollar(double amountInDollar) {
		this.amountInDollar = amountInDollar;
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

	public double getAchatMaxInDollar() {
		return getAchatMaxInDollar(this.currency);
	}
	
	public static double  getAchatMaxInDollar(String currency){
		SymbolConfig symbol = SymbolsConfig.getInstance().getSymbolConfig(currency);
		double maxDollar = symbol.getMaxTrade();
		if (maxDollar <= 0){
			return 0;
		}
		return maxDollar;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Balances getBalances() {
		return balances;
	}


	public void setBalances(Balances balances) {
		this.balances = balances;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
