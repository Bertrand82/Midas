package bg.panama.btc.trading.first;



import java.text.DecimalFormat;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.OrderManager;
import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.ActiveOrder;
import bg.panama.btc.model.ActiveOrders;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.v2.ITicker;
import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.model.v2.Tickers;
import bg.panama.btc.model.v2.TickersFactory;
import bg.panama.btc.swing.MidasGUI;
import bg.panama.btc.swing.ConfigFileProtected;
import bg.panama.btc.swing.SymbolsConfig;
import bg.util.HibernateUtil;



public class ThreadProcessTickers implements Runnable{

	private static final Logger loggerProcessCurrencies= LogManager.getLogger("processCurrencies");
	private static final Logger loggerTradeComparaison= LogManager.getLogger("tradeComparaison");
	
	private long timeSleeping = 60l * 1000l;
	private Thread t = new Thread(this);
	private boolean isOn = true;
	private Config config;
	private TickersFactory tickersFactory = TickersFactory.getInstance();
	
	public ThreadProcessTickers()  {	
		try {			
			//this.symbolsCurrenciesSelected = SymbolsConfig.getInstance().getSymbolsSelectedRequest();
			t.setDaemon(true);
			t.start();			
		} catch (Exception e) {
			isOn = false;
			this.timeSleeping=1l;
			awake();
			e.printStackTrace();
		}
	}
	//Balances balances_;

	public void run() {
		
			loggerProcessCurrencies.info( "run  DAILY_CHANGE_PERC	float	Amount that the price has changed expressed in percentage terms");
			while(isOn ){
				EntityManager em = null;
				try {
					 em = HibernateUtil.getEntityManagerFactory().createEntityManager();

					List<Tickers> lTickers = tickersFactory.getLastTickers(100,em);
					if (lTickers.size() > 0){
					AlgoProcessCurrencies algoProcess = new AlgoProcessCurrencies(lTickers);
					
					Ticker zBest = algoProcess.getTickerBest();
						
					loggerProcessCurrencies.info(""+algoProcess.toString());
					
					MidasGUI.getInstance().updateBestCurrencies(algoProcess);
					}
				} catch (Exception e) {
					log("Exception32: "+e.getClass()+" "+e.getMessage());
					System.err.println("Exception22 "+e.getClass()+" "+e.getMessage());
					e.printStackTrace();
				} catch (Throwable e) {
					log("Exception33: "+e.getClass()+" "+e.getMessage());
					e.printStackTrace();
				} finally{
					em.close();
				}
			//	checkEmergencySaveRequest();
				sleep(timeSleeping);
			//	checkEmergencySaveRequest();
			}
			log( "stop thread");
	}
	
	



	
	
	

	private static DecimalFormat decimalFormat = new DecimalFormat("0.000");
	
	
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
		loggerProcessCurrencies.info( s);
		loggerTradeComparaison.info(s);
	}

	
	
}