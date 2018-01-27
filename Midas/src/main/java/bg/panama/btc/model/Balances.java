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
import bg.panama.btc.swing.SymbolConfig;
import bg.panama.btc.swing.SymbolsConfig;
import bg.panama.btc.trading.first.Config;
import bg.panama.btc.trading.first.Order;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrency;
import bg.panama.btc.trading.first.Order.Side;

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
	
	public List<Order> processOrdersAchat() {
		List<Order> orders = new ArrayList<>();
		SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
		if (sessionCurrencies == null) {
			System.err.println("No sessionCurrencies for processing orders");
			return orders;
		}
		SessionCurrency sessionBest = sessionCurrencies.getBestEligible();
		if (sessionBest == null){
			return orders;
		}
		Order orderAchatBest = processSimpleOrderUsdToBest(sessionBest, 1000d);
		Balance balance = this.getBalance(sessionBest.getShortName());
		if (balance == null){
		}else if (orderAchatBest == null){
		}else {
			balance.addOrderAchat(orderAchatBest.getAmmount());
		}
		if (orderAchatBest != null){
			orders.add(orderAchatBest);
		}
		System.err.println("processOrdersAchat List Orders " + orders);
		return orders;
	}

	/**
	 * GEnere un ordre vers le best
	 * 
	 * @param sessionBest
	 * @return
	 */
	private Order processSimpleOrderUsdToBest(SessionCurrency sessionBest, double maxAmountInDolllar) {
		if (sessionBest == null) {
			return null;
		}

		SessionCurrencies sessionCurrencies = sessionBest.getSessionCurrencies();
		String currencyUSD = "usd";
		Balance balanceUsd = this.getBalance(currencyUSD);

		SymbolConfig symbolTickerBest = SymbolsConfig.getInstance().getSymbolConfig(sessionBest.getShortName());

		String currencyBest = sessionBest.getShortName();
		Balance balanceBest = getBalance(currencyBest);
		
		double amountMAxDollar = Balance.getAchatMaxInDollar(currencyBest);

		Order order = null;
		if (currencyBest.equalsIgnoreCase(currencyUSD)) {
			// PAs d'ordres! evidemment. Ne devrait jamais arriver
		} else if (sessionCurrencies.isModePanic()) {
			// Pas d'ordre. Le mode panic est géré par ailleurs mais il n'est
			// pas necessaire d'acheter
			logDebug("Is Mode Panic");
		} else if (sessionCurrencies.getAmbianceMarket().getNbPanic() > 0) {
			// Pas d'ordre. Le mode panic a fait disjoncter le system.
			logDebug("Mode Panic No removed");
		}  else if (this.totalCryptoAmountInDollar > Config.getInstance().getPlafondCryptoInDollar()*0.9) {
			// Pas d'ordre. Le mode panic a fait disjoncter le system.
			logDebug("Plafond crypto atteint");
		} else if (balanceUsd.getAvailableInDollar() < 50) {
			// minimum order size between 10-25 USD . Il n'y a plus de dollar
		} else if(balanceUsd.getAvailable() != balanceUsd.getAmount()){
			// Il y a des ordres "en cours", eventuellement des ordre d'achat de cette devise ....
			logDebug("Ordre achat usd en cours (available != amount)");
		} else if (sessionCurrencies.getNumero() < 10) {
			// Pas d'ordre: les filtres ne sont pas initialisés
			// Pas d'ordre: il y a assez d'ordre sur Balance Best
		} else if ( (balanceBest!=null) && (balanceBest.getAmountInDollar() > 100)) {
			// Le compte a déja été alimenté. A Modifier
		} else {
			double amountOrderInDollar = (balanceUsd.getAvailable()) * 0.9;
			if (symbolTickerBest.getMaxTrade() == 0) {

			} else {

				amountOrderInDollar = Math.min(amountMAxDollar, amountOrderInDollar);
				amountOrderInDollar = Math.min(Config.getInstance().getPlafondCryptoInDollar(), amountOrderInDollar);
			}
			amountOrderInDollar = Math.min(Config.getInstance().getPlafondCurrencyInDollard(), amountOrderInDollar); // ECRETAGE POUR QUALIF
			double price = sessionBest.getTicker_Z_1().getLastPrice();
			double amount = amountOrderInDollar/price;
			order = new Order( sessionBest.getShortName(), amount, Order.Side.buy, Order.TypeChoicePrice.fromTickers);
			order.setComment("orderToBest limit : "+maxAmountInDolllar+"");
		}
		return order;
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

	public List<Order> saveAllInDollar(SessionCurrencies session) {

		List<Order> orders = new ArrayList<>();
		for (Balance balance : this.lBalancesExchange) {
			String currency = balance.getCurrency();
			if ("usd".equalsIgnoreCase(currency)) {
				// PAs d'ordres
			} else if (balance.getAvailableInDollar() < 30) {
				// minimum order size between 10-25 USD
			} else {
				Order order = new Order(currency,  balance.getAvailable(),Order.Side.sell, Order.TypeChoicePrice.panic);
				orders.add(order);
			}
		}
		System.err.println(" saveAllInDollar orders " + orders.size());
		return orders;
	}

	public double getAvailableUSD() {
		return getBalanceUSD().getAvailable();
	}

	public double getAmountUSD() {
		return getBalanceUSD().getAmount();
	}

	public List<Order> processOrdersVente() {
		List<Order> orders = new ArrayList<>();
		SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
		if (sessionCurrencies == null){
			return orders;
		}
		if (sessionCurrencies.isModePanic()){
			// C'ets traité par ailleurs
			return orders;
		}
		for (Balance balance : this.lBalancesExchange) {
			String currency = balance.getCurrency();
			if ("usd".equalsIgnoreCase(currency)) {
				// PAs d'ordres =))
			} else if (balance.getAvailableInDollar() < 30) {
				// minimum order size between 10-25 USD
			} else {
				SessionCurrency sc = sessionCurrencies.getSessionCurrency_byShortName(currency);
				
				if (sc== null) {
					System.err.println("No sessionCurrency for :"+currency);
				}else {
					SessionCurrency.EtatSTOCHASTIQUE etat_10mn = SessionCurrency.getStochastique(sc.getStochastique_10mn());
					System.err.println("processOrdersVente "+currency+"  "+etat_10mn+"    vendre: "+etat_10mn.vendre);
					if (etat_10mn.vendre){
						Order orderVente  = new Order(sc.getShortName(),balance.getAvailable(),Order.Side.sell, Order.TypeChoicePrice.fromBookOrder);
						balance.addOrderVente(orderVente.getAmmount());
						orders.add(orderVente);
					}
				}
			}
		}
		System.err.println("processOrdersVente orders " + orders.size());
		return orders;
	}

}
