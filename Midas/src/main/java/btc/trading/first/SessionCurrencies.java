package btc.trading.first;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import btc.model.Balance;
import btc.model.Balances;
import btc.model.v2.ITicker;
import btc.model.v2.Ticker;
import btc.model.v2.Tickers;

public class SessionCurrencies implements Serializable {

	private static final long serialVersionUID = 1L;

	private Balances balancesCurrent;

	List<SessionCurrency> lSessionCurrency = new ArrayList<SessionCurrency>();

	public SessionCurrencies(Tickers tickers) {
		super();
		for (ITicker ticker : tickers.getlTickers()) {
			SessionCurrency z_1_Currency = new SessionCurrency(ticker);
			lSessionCurrency.add(z_1_Currency);
		}

	}

	public void update(Tickers tickers) {
		for (ITicker ticker : tickers.getlTickers()) {
			SessionCurrency z_1_Currency = getZ_1_Currency_byName(ticker.getName());
			z_1_Currency.update(ticker);

		}
	}

	private SessionCurrency getZ_1_Currency_byName(String name) {
		for (SessionCurrency z : lSessionCurrency) {
			if (name.equalsIgnoreCase(z.getName())) {
				return z;
			}
		}
		return null;
	}

	public List<SessionCurrency> getListOrder(Comparator<SessionCurrency> comparator) {
		Collections.sort(lSessionCurrency, comparator);
		return lSessionCurrency;

	}

	public List<ITicker> getListOrder_byDailyChangePerCent() {
		List<ITicker> listNew = new ArrayList<ITicker>();
		listNew.addAll(lSessionCurrency);
		Collections.sort(listNew, ITicker.comparatorDailyChangePercent);
		return listNew;

	}

	public synchronized ITicker getTickerBest() {
		ITicker z = getListOrder_byDailyChangePerCent().get(0);
		return z;
	}

	public synchronized ITicker getTickerWorse() {
		ITicker z = getListOrder_byDailyChangePerCent().get(lSessionCurrency.size() - 1);
		return z;
	}

	public double getLastPrice(String currency) {
		for (SessionCurrency zcurrency : this.lSessionCurrency) {
			if (zcurrency.getShortName().equalsIgnoreCase(currency)) {
				double lastPrice = zcurrency.getLastPrice();
				return lastPrice;
			}
		}
		return 0;
	}

	public double getDaylyChangePerCent(String currency) {
		for (SessionCurrency zcurrency : this.lSessionCurrency) {
			if (zcurrency.getShortName().equalsIgnoreCase(currency)) {
				double daylyChangePerCent = zcurrency.getDaylyChangePerCent();
				return daylyChangePerCent;
			}
		}
		return 0;
	}

	public List<SessionCurrency> getlSessionCurrency() {
		return lSessionCurrency;
	}

	public Balances getBalancesCurrent() {
		return balancesCurrent;
	}

	public void setBalancesCurrent(Balances balancesCurrent) {
		this.balancesCurrent = balancesCurrent;
	}

	public Balance getBalance(String currency) {
		if (this.balancesCurrent == null) {
			return null;
		}
		return balancesCurrent.getBalance(currency);
	}

	public SessionCurrency getTickerByCurrency(String currency) {
		for (SessionCurrency zcurrency : this.lSessionCurrency) {
			if (zcurrency.getShortName().equalsIgnoreCase(currency)) {

				return zcurrency;
			}
		}
		return null;
	}

	public ITicker getBestEligible() {
		for (int i = 0; i < this.getListOrder_byDailyChangePerCent().size(); i++) {
			SessionCurrency sc = (SessionCurrency) this.getListOrder_byDailyChangePerCent().get(i);
			if (sc.isEligible()) {
				return sc;
			}
		}
		System.err.println("Pas de currency eligible");
		return null;
	}

}
