package bg.panama.btc.trading.first;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.model.v2.Tickers;

public class AlgoProcessCurrencies {

	private List<Tickers> listTickers;
	private List<AlgoProcessCurrency> listAlgoProcessCurrency = new ArrayList<>();
	private Tickers tickersLast;

	public AlgoProcessCurrencies(List<Tickers> lTickers) {
		this.listTickers = lTickers;

		tickersLast = lTickers.get(0);
		for (Ticker ticker : tickersLast.getlTickers()) {
			List<Ticker> lTickerCurrency = this.getListTickersByCurrency(ticker.getName());
			
			AlgoProcessCurrency apc = new AlgoProcessCurrency(lTickerCurrency, ticker.getName());
			listAlgoProcessCurrency.add(apc);
		}
	}

	private List<Ticker> getListTickersByCurrency(String name) {
		List<Ticker> l = new ArrayList<>();
		for (Tickers tickers : this.listTickers) {
			Ticker t = tickers.getTickerByName(name);
			if (t != null) {
				l.add(t);
			}
		}
		return l;
	}

	public Ticker getTickerBestByMoyenne() {
		List<AlgoProcessCurrency> l = new ArrayList<>();
		l.addAll(listAlgoProcessCurrency);
		Comparator<AlgoProcessCurrency> comparator = AlgoProcessCurrency.COMPARATOR_MOYENNE;
		Collections.sort(l, comparator );
		Ticker zBest =  listAlgoProcessCurrency.get(0).getLastTicker();
		return zBest;
	}

	

}
