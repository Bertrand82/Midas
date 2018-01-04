package btc.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import btc.BitfinexClient.EnumService;
import btc.model.v2.ITicker;
import btc.model.v2.Ticker;
import btc.trading.first.Order;
import btc.trading.first.SessionCurrencies;
import btc.trading.first.SessionCurrency;

public class Balances implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private static final Logger loggerTradeBalance = LogManager.getLogger("tradeBalance");
	private List<Balance> lBalances = new ArrayList<>();
	private List<Balance> lBalancesExchange = new ArrayList<>();

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
	private double totalDollar = 0;

	public List<Order> process(SessionCurrencies session) {
		ITicker tickerBest = session.getBestEligible();
		List<Order> orders = new ArrayList<>();
		totalDollar = 0;

		for (Balance balance : this.lBalancesExchange) {
			String currency = balance.getCurrency();
			SessionCurrency tickerCurrent = session.getTickerByCurrency(currency);
			double available = balance.getAmount();

			double lastPrice = session.getLastPrice(currency);
			double percent = session.getDaylyChangePerCent(currency);
			double deltaPercent = (tickerBest.getDaylyChangePerCent() - percent);
			double availableInDollar = lastPrice * available;
			loggerTradeBalance.info("Devise :" + currency + "\t|dayly change per cent : " + df2.format(percent * 100)
					+ "| available :" + df.format(available) + "\t| daily " + df.format(lastPrice) + "\t| in dollar "
					+ df.format(availableInDollar));
			totalDollar += availableInDollar;
			balance.setLastPrice(lastPrice);
			balance.setPercent(percent);
			balance.setAvailableInDollar(availableInDollar);
			if (tickerBest.getShortName().equalsIgnoreCase(currency)) {
				// PAs d'ordres
			} else if (Math.abs(deltaPercent) < 0.02) {
				// Introduction de stabilité (eviter les yoyo)
			} else if (availableInDollar < 50) {
				// minimum order size between 10-25 USD
			} else if (session.getNumero() < 10) {
				// Pas d'ordre: les filtres ne sont pas initialisés
			} else {
				Order order = new Order(currency, tickerBest.getShortName(), available*0.9);
				orders.add(order);
			}
		}
		loggerTradeBalance.info("Total available In dollar : " + df.format(totalDollar));
		loggerTradeBalance.info("List Orders " + orders);
		return orders;
	}

	@Override
	public String toString() {
		return "Balances [lBalances=" + lBalances + "]";
	}

	public double getTotalDollar() {
		return totalDollar;
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

			totalDollar += availableInDollar;
			balance.setLastPrice(lastPrice);
			balance.setPercent(percent);
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
		return orders;
	}
}
