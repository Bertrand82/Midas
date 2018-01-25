package bg.panama.btc.trading.first;



import java.text.DecimalFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.OrderFactory;
import bg.panama.btc.model.ActiveOrder;
import bg.panama.btc.model.ActiveOrders;
import bg.panama.btc.model.v2.Tickers;
import bg.panama.btc.model.v2.TickersFactory;
import bg.panama.btc.swing.ConfigFileProtected;
import bg.panama.btc.swing.MidasGUI;
import bg.panama.btc.swing.SymbolsConfig;
import bg.util.HibernateUtil;



public class ThreadFetchTickers implements Runnable{

	private static final Logger loggerTrade= LogManager.getLogger("trade");
	private static final Logger loggerTradeComparaison= LogManager.getLogger("tradeComparaison");
	private static final Logger loggerTradeBalance= LogManager.getLogger("tradeBalance");
	
	long timeSleeping = 60l * 1000l;
	Thread t = new Thread(this);
	String symbolsCurrenciesSelected;
	boolean isOn = true;
	BitfinexClient bitfinexClient;
	SessionCurrencies sessionCurrencies;
	Config config = Config.getInstance();
	public ThreadFetchTickers()  {	
		
		try {			
			this.symbolsCurrenciesSelected = SymbolsConfig.getInstance().getSymbolsSelectedRequest();
			String pwd = config.getPassword();
			ConfigFileProtected pcf  = new ConfigFileProtected(pwd);
			bitfinexClient= new BitfinexClient();
			t.setName("FetchTickersBitfinex");
			t.setDaemon(true);
			t.start();
			
		} catch (Exception e) {
			isOn = false;
			this.timeSleeping=1l;
			awake();
			e.printStackTrace();
		}
	}
	
	TickersFactory tickersFactory = TickersFactory.getInstance();
	public void run() {
		
			loggerTrade.info( "run  DAILY_CHANGE_PERC	float	Amount that the price has changed expressed in percentage terms");
			while(isOn ){
				try {
					Tickers tickers = fetchTickers();
					ServiceCurrencies.getInstance().setTickersCurrent(tickers);
					if (sessionCurrencies == null){
						sessionCurrencies = SessionCurrenciesFactory.instance.getLastSessionCurrencies();
					}
					if (sessionCurrencies == null){
						sessionCurrencies = new SessionCurrencies(tickers);
					}else {
						sessionCurrencies = (SessionCurrencies) sessionCurrencies.clone();
						sessionCurrencies.update(tickers);
					}
					loggerTrade.info(""+tickers.toString());
					
					
					if (sessionCurrencies.isModePanic()){
						emergencySaveInDollar("ThreadFetchTickers");
					}
					MidasGUI.getInstance().updateThread();
					EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
					EntityManager em = emf.createEntityManager();
					SessionCurrenciesFactory.instance.persists(em,sessionCurrencies,tickers);
					em.close();
					ServiceCurrencies.getInstance().setSessionCurrencies(sessionCurrencies);
				} catch (Exception e) {
					log("Exception22: "+e.getClass()+" "+e.getMessage());
					System.err.println("Exception22 "+e.getClass()+" "+e.getMessage());
					e.printStackTrace();
				} catch (Throwable e) {
					log("Exception23: "+e.getClass()+" "+e.getMessage());
					e.printStackTrace();
				}
				sleep(timeSleeping);
			}
			log( "stop thread");
	}
	
	



	private static DecimalFormat decimalFormat = new DecimalFormat("0.000");
	

	private Tickers fetchTickers(){
		try {
			return   (Tickers) bitfinexClient.serviceProcess(EnumService.tickersV2,"",symbolsCurrenciesSelected);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public void stop(String from){
		log( "stop request from "+from);
		this.isOn =false;
		this.awake();
	}
	public synchronized void awake() {
		notifyAll();
	}
	
	private synchronized void sleep(long time){
		try {
			
			wait(time);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void log(String s){
		loggerTrade.info( s);
		loggerTradeComparaison.info(s);
	}

	public SessionCurrencies getSesionCurrencies() {
		return sessionCurrencies;
	}

	public void emergencySaveInDollar(String from) {
		log("emergy save request from "+from);
		OrderFactory.getInstance().emergencySaveInDollar( from);
	}

	public Config getConfig() {
		return config;
	}

	
	public ActiveOrders fetchOrders() throws Exception{
		ActiveOrders o =(ActiveOrders) this.bitfinexClient.serviceProcess(EnumService.orders,"","");
		System.out.println("orders :"+o);
		return o;
	}





	public void displayActiveOrders() throws Exception{
		ActiveOrders aOrders = fetchOrders();
		for(ActiveOrder ao: aOrders.getlOrders()){
			displayActiveOrder(ao);
		}
	}





	private void displayActiveOrder(ActiveOrder ao) {
		System.err.println("Cancel Order "+ao+" No Implemented yet");
	}





	public void cancelAllOrders() throws Exception{
		Object o = this.bitfinexClient.serviceProcess(EnumService.cancelAllOrders,"","");
		System.out.println("orders :"+o);
		
	}
	
}
