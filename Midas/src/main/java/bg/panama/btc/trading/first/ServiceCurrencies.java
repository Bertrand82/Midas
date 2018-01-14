package bg.panama.btc.trading.first;

import java.util.Currency;

import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.model.v2.Tickers;

public class ServiceCurrencies {

	
	private Tickers tickersCurrent;
	
	private static ServiceCurrencies instance = new ServiceCurrencies();

	public static ServiceCurrencies getInstance() {
		return instance;
	}

	private ServiceCurrencies() {
		super();
	}

	public Tickers getTickersCurrent() {
		return tickersCurrent;
	}

	public void setTickersCurrent(Tickers tickersCurrent) {
		this.tickersCurrent = tickersCurrent;
	}
	
	public double getPriceInDollar(String currency) {
		if (this.tickersCurrent == null){
			return 0d;
		}
		Ticker t = tickersCurrent.getTickerByName("t"+currency+"usd");
		if (t == null){
			if (currency.equalsIgnoreCase("usd")){
				
			}else {
				System.err.println("No Ticker- for >>>"+currency);
			}
			return 0;
		}
		return t.getLastPrice();
	}
	
}
