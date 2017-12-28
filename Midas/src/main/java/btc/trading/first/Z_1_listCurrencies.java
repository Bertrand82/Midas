package btc.trading.first;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import btc.model.v2.Ticker;
import btc.model.v2.Tickers;


public class Z_1_listCurrencies implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final Comparator<Z_1_Currency> comparatorDailyChangePercent = new Comparator<Z_1_Currency>() {

		@Override
		public int compare(Z_1_Currency o1, Z_1_Currency o2) {
			Double d = o2.daylyChangePerCent;
			return d.compareTo(o1.daylyChangePerCent);
		}
	};
	
	List<Z_1_Currency> lTickers_1 = new ArrayList<Z_1_Currency>();

	public Z_1_listCurrencies(Tickers tickers) {
		super();
		for(Ticker ticker :tickers.getlTickers()){
			Z_1_Currency z_1_Currency = new Z_1_Currency(ticker);			
			lTickers_1.add(z_1_Currency);
		}
	}

	public void update(Tickers tickers) {
		for(Ticker ticker :tickers.getlTickers()){
			Z_1_Currency z_1_Currency = getZ_1_Currency_byName(ticker.getName());
			z_1_Currency.update(ticker);
			lTickers_1.add(z_1_Currency);
		}
	}

	private Z_1_Currency getZ_1_Currency_byName(String name) {
		for(Z_1_Currency z : lTickers_1){
			if (name.equalsIgnoreCase(z.getName())){
				return z;
			}
		}
		return null;
	}
	

	public List<Z_1_Currency> getListOrder(Comparator<Z_1_Currency> comparator){
		Collections.sort(lTickers_1,comparator);
		return lTickers_1;

	}
	
	public synchronized List<Z_1_Currency> getListOrder_byDailyChangePerCent(){
		Collections.sort(lTickers_1,comparatorDailyChangePercent);
		return lTickers_1;

	}

	public synchronized Z_1_Currency getTickerBest() {
		Z_1_Currency z =getListOrder_byDailyChangePerCent().get(0);
		return z;
	}

	public synchronized Z_1_Currency getTickerWorse() {
		Z_1_Currency z =getListOrder_byDailyChangePerCent().get(lTickers_1.size()-1);
		return z;
	}
}
