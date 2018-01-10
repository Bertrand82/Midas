package btc.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import btc.BitfinexClient.EnumService;
import btc.swing.SymbolConfig;
import btc.swing.SymbolsConfig;
import btc.trading.first.Order;
import btc.trading.first.SessionCurrencies;
import btc.trading.first.SessionCurrency;

public class Balances implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private static final Logger loggerTradeBalance = LogManager.getLogger("tradeBalance");
	private List<Balance> lBalances = new ArrayList<>();
	private List<Balance> lBalancesExchange = new ArrayList<>();
	private double availableUSD;
	private double amountUSD;
	public Balances(JSONObject jo) throws Exception {
		try {
			JSONArray array = jo.getJSONArray(EnumService.balances.key);
			for (int i = 0; i < array.length(); i++) {
				Object o = array.get(i);
				Balance balance = new Balance((JSONObject) array.get(i));
				this.lBalances.add(balance);
				if (balance.isTypeEchange()) {
					this.lBalancesExchange.add(balance);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	DecimalFormat df = new DecimalFormat("0000000.00");
	DecimalFormat df2 = new DecimalFormat("00.00");
	private double totalAvailableDollar = 0;
	private double totalAmountDollar = 0;

	public List<Order> process(SessionCurrencies session) {
		
		SessionCurrency tickerBest = session.getBestEligible(this);
		String currencyBest = tickerBest.getName();
		SymbolConfig symbolTickerBest = SymbolsConfig.getInstance().getSymbolConfig(tickerBest.getShortName());
		Balance balanceBest = getBalance(currencyBest);
		List<Order> orders = new ArrayList<>();
		totalAvailableDollar = 0;
		totalAmountDollar = 0;

		for (Balance balance : this.lBalancesExchange) {
			String currency = balance.getCurrency();
			SessionCurrency tickerCurrent = session.getTickerByCurrency(currency);
			
			if(tickerCurrent == null){
				if (currency.equalsIgnoreCase("usd")){
					tickerCurrent = SessionCurrency.SessionCurrencyUSD;
					this.availableUSD = balance.available;
					this.amountUSD = balance.amount;
					System.err.println("USD amount :"+balance.amount+"  available :"+balance.available);
				}else {
				System.err.println("No ticker for "+currency);
				break;
				}
			}
			double available = balance.getAvailable();
			double amount = balance.getAmount();

			double lastPrice = session.getLastPrice(currency);
			double percentDayly_DEPRECATED = session.getDaylyChangePerCent(currency);
			double percentHourlyByDay = tickerCurrent.getHourlyChangePerCentByDay();
			double deltaPercent = (tickerBest.getHourlyChangePerCentByDay() - tickerCurrent.getHourlyChangePerCentByDay());
			double availableInDollar = lastPrice * available;
			double amountInDollar = lastPrice * amount;
			loggerTradeBalance.info("Devise :" + currency + "\t|Hourly change per cent/D : " + df2.format(percentHourlyByDay )
					+ "| available :" + df.format(available) + "\t| daily " + df.format(lastPrice) + "\t| in dollar "
					+ df.format(availableInDollar));
			totalAvailableDollar += availableInDollar;
			totalAmountDollar += amountInDollar;
			balance.setLastPrice(lastPrice);
			balance.setPercentHourlyByDay(percentHourlyByDay);
			balance.setAvailableInDollar(availableInDollar);
			
			
			if (tickerBest.getShortName().equalsIgnoreCase(currency)) {
				// PAs d'ordres! evidemment
			} else if (Math.abs(deltaPercent) < 0.02) {
				// Introduction de stabilité (eviter les yoyo)
			} else if (availableInDollar < 50) {
				// minimum order size between 10-25 USD
			} else if (session.getNumero() < 10) {
			
				// Pas d'ordre: les filtres ne sont pas initialisés
				// Pas d'ordre: il y a assez d'ordre sur Balance Best
			} else {
				double amountOrder = ( balance.available)*0.9;
			    if (symbolTickerBest.getMaxTrade() == 0){
			    	
			    }else {
			    	double amountMAx = balanceBest.getAchatMax();
			    	amountOrder = Math.min(amountMAx, amountOrder);
			    }
			  
				Order order = new Order(currency, tickerBest.getShortName(), amountOrder);
				orders.add(order);
			}
		}
		loggerTradeBalance.info("Total available In dollar : " + df.format(totalAvailableDollar));
		loggerTradeBalance.info("List Orders " + orders);
		return orders;
	}

	@Override
	public String toString() {
		return "Balances [lBalances=" + lBalances + "]";
	}

	public double getTotalAvailableDollar() {
		return totalAvailableDollar+availableUSD;
	}

	public double getTotalAmountDollar() {
		return totalAmountDollar +amountUSD;
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
			SessionCurrency tickerCurrent = session.getTickerByCurrency(currency);
			double available = balance.getAmount();

			double lastPrice = session.getLastPrice(currency);
			double percent = session.getDaylyChangePerCent(currency);
			double availableInDollar = lastPrice * available;

			totalAvailableDollar += availableInDollar;
			balance.setLastPrice(lastPrice);
			balance.setPercentHourlyByDay(percent);
			balance.setAvailableInDollar(availableInDollar);
			if ("usd".equalsIgnoreCase(currency)) {
				// PAs d'ordres
			} else if (availableInDollar < 25) {
				// minimum order size between 10-25 USD
			} else {
				
				Order order = new Order(currency, "usd", available);
				orders.add(order);
			}			
		}
		System.err.println(" saveAllInDollar orders "+orders.size());
		return orders;
	}

	public double getAvailableUSD() {
		return availableUSD;
	}

	public double getAmountUSD() {
		return amountUSD;
	}
	
}
