package bg.panama.btc.trading.first;

import java.util.Comparator;
import java.util.List;

import bg.panama.btc.model.v2.Ticker;

public class AlgoProcessCurrency {

	public static final Comparator<AlgoProcessCurrency> COMPARATOR_MOYENNE = new Comparator<AlgoProcessCurrency>() {

		@Override
		public int compare(AlgoProcessCurrency o1, AlgoProcessCurrency o2) {
			return o1.moyenne_15_mn.compareTo(o2.moyenne_15_mn);
		}
	};
	private static final int MINUTE = 60*1000;;
	private String name ;
	List<Ticker> lTickerCurrency  ;
	private Double moyenne_15_mn =0d;
	
	public AlgoProcessCurrency(List<Ticker> lTickerCurrency, String name) {
		this.name = name;
		this.lTickerCurrency = lTickerCurrency;
	
		this.moyenne_15_mn = getMoyenne(15);
	}
	private Double getMoyenne(int i) {
		double total = 0;
		int n =0;
		for(Ticker t : this.lTickerCurrency){
			total += t.getLastPrice();
			n ++;
			long dt = System.currentTimeMillis() - t.getDate().getTime();
			if (dt > i * MINUTE){
				break;
			}
		}
		double moyenne = total/n;
		return moyenne;
	}
	public Ticker getLastTicker() {
		return lTickerCurrency.get(0);
	}
	public String getName() {
		return name;
	}

}
