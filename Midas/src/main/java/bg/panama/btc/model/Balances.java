package bg.panama.btc.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.operation.Operation;
import bg.panama.btc.model.operation.OperationsManager;
import bg.panama.btc.model.operation.Order;
import bg.panama.btc.model.operation.Order.Side;
import bg.panama.btc.swing.SymbolConfig;
import bg.panama.btc.swing.SymbolsConfig;
import bg.panama.btc.trading.first.Config;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrency;

@Entity
public class Balances implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	
	private static final long serialVersionUID = 1L;
	private static final Logger loggerTradeBalance = LogManager.getLogger("tradeBalance");
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="balances")
	private List<Balance> lBalances = new ArrayList<>();
	@Transient
	private List<Balance> lBalancesExchange = new ArrayList<>();
	private double totalAvailableDollar = 0;
	private double totalAmountDollar = 0;
	private double totalCryptoAmountInDollar=0;
	
	
	public Balances() {
		
	}
	public Balances(JSONObject jo) throws Exception {
		try {
			JSONArray array = jo.getJSONArray(EnumService.balances.key);
			for (int i = 0; i < array.length(); i++) {
				Object o = array.get(i);
				Balance balance = new Balance((JSONObject) array.get(i),this);
				this.lBalances.add(balance);
				if (balance.isTypeEchange()) {
					this.lBalancesExchange.add(balance);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		calculTotaux();
		loggerTradeBalance.info("Total available In dollar : " + df.format(totalAvailableDollar));
	}

	private Balance getBalanceUSD() {
		return getBalance("usd");
	}

	private void calculTotaux() {
		this.totalAmountDollar = 0;
		this.totalAvailableDollar = 0;
		this.totalCryptoAmountInDollar=0;
		for (Balance balance : this.lBalancesExchange) {
			this.totalAmountDollar += balance.getAmountInDollar();
			this.totalAvailableDollar += balance.getAvailableInDollar();
			if (!balance.getCurrency().equals("usd")){
				totalCryptoAmountInDollar += balance.getAmountInDollar();
			}
		}
		System.out.println("totalAmountDollar :"+totalAmountDollar+" | totalCryptoAmountInDollar :"+totalCryptoAmountInDollar);
	}

	private final static DecimalFormat df = new DecimalFormat("0000000.00");
	private final static DecimalFormat df2 = new DecimalFormat("00.00");
	
	public double getTotalCryptoAmountInDollar() {
		return totalCryptoAmountInDollar;
	}
	
	
	/**
	 * Les achat dependes du marché ... et des fonds disponibles.
	 * Il est donc logique que cette methode soit ici;
	 * @return
	 */
	public void processOrdersAchat() {
		List<Operation> orders = new ArrayList<>();
		SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
		if (sessionCurrencies == null) {
			System.err.println("No sessionCurrencies for processing orders");
			return ;
		}
		SessionCurrency sessionBest = sessionCurrencies.getBestEligible();
		if (sessionBest == null){
			return ;
		}
		Operation orderAchatBest = OperationsManager.processSimpleOrderUsdToBest(this,sessionBest, 1000d);
		Balance balance = this.getBalance(sessionBest.getShortName());
		if (balance == null){
		}else if (orderAchatBest == null){
		}else {
			balance.addOrderAchat(orderAchatBest.getOrderAchat().getAmmount());
		}
		if (orderAchatBest != null){
			orders.add(orderAchatBest);
		}
		System.err.println("processOrdersAchat List Orders " + orders);
		OperationsManager.processOperationsAchat(orders);
	}

	

	@Override
	public String toString() {
		return "Balances [lBalances=" + lBalances + "]";
	}
	private static boolean debug = true;
	private static void logDebug(String s) {
	
		if (debug){
			System.out.println("tDebug :"+s);
		}
	}

	public double getTotalAvailableDollar() {
		return totalAvailableDollar;
	}

	public double getTotalAmountDollar() {
		return totalAmountDollar;
	}

	public List<Balance> getlBalancesExchange() {
		return lBalancesExchange;
	}

	public Balance getBalance(String currency) {
		for (Balance balance : this.lBalancesExchange) {
			if (balance.getCurrency().equalsIgnoreCase(currency)) {
				return balance;
			}
		}
		return null;
	}


	public double getAvailableUSD() {
		return getBalanceUSD().getAvailable();
	}

	public double getAmountUSD() {
		return getBalanceUSD().getAmount();
	}
	public void setTotalAvailableDollar(double totalAvailableDollar) {
		this.totalAvailableDollar = totalAvailableDollar;
	}

	
}
