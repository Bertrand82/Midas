package btc.trading.first;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import btc.model.v2.ITicker;
import btc.model.v2.Ticker;
import btc.model.v2.Tickers;


public class Z_1_listCurrencies implements Serializable {
	
	private static final long serialVersionUID = 1L;


	
	List<Z_1_Currency> lTickers_1 = new ArrayList<Z_1_Currency>();
	
	public Z_1_listCurrencies(Tickers tickers) {
		super();
		for(ITicker ticker :tickers.getlTickers()){
			Z_1_Currency z_1_Currency = new Z_1_Currency(ticker);			
			lTickers_1.add(z_1_Currency);
		}
		
	}

	
	public void update(Tickers tickers) {
		for(ITicker ticker :tickers.getlTickers()){
			Z_1_Currency z_1_Currency = getZ_1_Currency_byName(ticker.getName());
			z_1_Currency.update(ticker);
			
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
	
	public  List<ITicker> getListOrder_byDailyChangePerCent(){
		List<ITicker> listNew = new ArrayList<ITicker>();
		listNew.addAll(lTickers_1);
		Collections.sort(listNew,ITicker.comparatorDailyChangePercent);
		return listNew;

	}

	public synchronized ITicker getTickerBest() {
		ITicker z =getListOrder_byDailyChangePerCent().get(0);
		return z;
	}

	public synchronized ITicker getTickerWorse() {
		ITicker z =getListOrder_byDailyChangePerCent().get(lTickers_1.size()-1);
		return z;
	}

	public double getLastPrice( String currency) {
		for(Z_1_Currency  zcurrency : this.lTickers_1){
			if (zcurrency.getShortName().equalsIgnoreCase(currency)){
				double lastPrice = zcurrency.getLastPrice();
				return lastPrice;
			}
		}
		return 0;
	}
	
	public double getDaylyChangePerCent( String currency) {
		for(Z_1_Currency  zcurrency : this.lTickers_1){
			if (zcurrency.getShortName().equalsIgnoreCase(currency)){
				double daylyChangePerCent = zcurrency.getDaylyChangePerCent();
				return daylyChangePerCent;
			}
		}
		return 0;
	}
}
