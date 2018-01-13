package bg.panama.btc.trading.first;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import bg.panama.btc.model.Balance;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.v2.ITicker;
import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.model.v2.Tickers;


@Entity
public class SessionCurrencies implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue
	private long id;

	private final Date timeStart = new Date();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="sessionCurrencies")
	List<SessionCurrency> lSessionCurrency = new ArrayList<SessionCurrency>();
	private int numero =0;
	
	public SessionCurrencies() {
		
	}
	
	public SessionCurrencies(Tickers tickers) {
		super();
		for (ITicker ticker : tickers.getlTickers()) {
			SessionCurrency z_1_Currency = new SessionCurrency((Ticker) ticker,this);
			lSessionCurrency.add(z_1_Currency);
		}		
	}

	public void update(Tickers tickers) {
		numero++;
		for (ITicker ticker : tickers.getlTickers()) {
			SessionCurrency sessionCurrency = getSessionCurrency_byName(ticker.getName());
			sessionCurrency.update((Ticker) ticker);
		}
	}

	private SessionCurrency getSessionCurrency_byName(String name) {
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

	public static final Comparator<SessionCurrency> comparatorDailyChangePercent = new Comparator<SessionCurrency>() {

		@Override
		public int compare(SessionCurrency o1, SessionCurrency o2) {
			Double d = o2.getHourlyChangePerCentByDay();
			return d.compareTo(o1.getHourlyChangePerCentByDay());
		}
	};
	
	public static final Comparator<SessionCurrency> comparatorHourlyChangePercentByDay = new Comparator<SessionCurrency>() {

		@Override
		public int compare(SessionCurrency o1, SessionCurrency o2) {
			Double d = o2.getHourlyChangePerCentByDay();
			return d.compareTo(o1.getHourlyChangePerCentByDay());
		}
	};
	public List<SessionCurrency> getListOrder_byDailyChangePerCent__DEPRECATED() {
		List<SessionCurrency> listNew = new ArrayList<SessionCurrency>();
		listNew.addAll(lSessionCurrency);
		Collections.sort(listNew, comparatorDailyChangePercent);
		return listNew;

	}
	
	
	

	public List<SessionCurrency> getListOrder_byHourlyChangePerCentByDay() {
		List<SessionCurrency> listNew = new ArrayList<SessionCurrency>();
		listNew.addAll(lSessionCurrency);
		Collections.sort(listNew, comparatorHourlyChangePercentByDay);
		return listNew;

	}

	public synchronized SessionCurrency getTickerBest() {
		SessionCurrency z = getListOrder_byHourlyChangePerCentByDay().get(0);
		return z;
	}

	public synchronized SessionCurrency getTickerWorse() {
		SessionCurrency z = getListOrder_byHourlyChangePerCentByDay().get(lSessionCurrency.size() - 1);
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

	public double getHourlyChangePerCentByDay(String currency) {
		for (SessionCurrency zcurrency : this.lSessionCurrency) {
			if (zcurrency.getShortName().equalsIgnoreCase(currency)) {
				double daylyChangePerCent = zcurrency.getHourlyChangePerCentByDay();
				return daylyChangePerCent;
			}
		}
		return 0;
	}

	public List<SessionCurrency> getlSessionCurrency() {
		return lSessionCurrency;
	}


	public SessionCurrency getTickerByCurrency(String currency) {
		for (SessionCurrency zcurrency : this.lSessionCurrency) {
			if (zcurrency.getShortName().equalsIgnoreCase(currency)) {

				return zcurrency;
			}
		}
		return null;
	}
    SessionCurrency sessionCurrencyBestEligible;
	public SessionCurrency getSessionCurrencyBestEligible() {
		return sessionCurrencyBestEligible;
	}

	
	public SessionCurrency getBestEligible(Balances balancesCurrent) {
		List<SessionCurrency> list = this.getListOrder_byHourlyChangePerCentByDay();
		// Liste ordonnée les premieres sont lthe best ... si elles sont eligibles
		if (balancesCurrent == null){
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			
			SessionCurrency sc = (SessionCurrency) list.get(i);
			Balance b = balancesCurrent.getBalance(sc.getShortName());
			if (sc.isEligible() && !b.isOverLimit()) {
				this.sessionCurrencyBestEligible =sc;
				return sc;
			}
		}
		System.err.println("getBestEligible Pas de currency eligible");
		return null;
	}

	public List<Order> saveAllInDollar(Balances balancesCurrent) {
		System.err.println("Save All In Dollar start");	
		return balancesCurrent.saveAllInDollar(this);
	}

	public void saveConfiguration() {
		System.err.println("saveConfiguration");
	}

	public void updateWithArchive(SessionCurrencies sessionArchive) {
		for(SessionCurrency sc : this.lSessionCurrency){
			String name = sc.getName();
			SessionCurrency sArchive = sessionArchive.getSessionCurrency_byName(name);
			sc.updateWithArchive(sArchive);
		}
	}

	public int getNumero() {
		return numero;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		
		SessionCurrencies s = new SessionCurrencies();
		s.numero = numero;
		s.sessionCurrencyBestEligible = sessionCurrencyBestEligible;
		for(SessionCurrency sc : lSessionCurrency){
			SessionCurrency sc2 =(SessionCurrency) sc.clone();
			s.lSessionCurrency.add(sc2);
			sc2.setSessionCurrencies(s);
		}		
		return s;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

}
