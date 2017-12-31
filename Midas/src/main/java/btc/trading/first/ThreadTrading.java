package btc.trading.first;



import java.text.DecimalFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import btc.BitfinexClient;
import btc.BitfinexClient.EnumService;
import btc.OrderManager;
import btc.model.Balances;
import btc.model.v2.ITicker;
import btc.model.v2.Tickers;
import btc.swing.MidasGUI;
import btc.swing.ProtectedConfigFile;
import btc.swing.SymbolsConfig;



public class ThreadTrading implements Runnable{

	private static final Logger loggerTrade= LogManager.getLogger("trade");
	private static final Logger loggerTradeComparaison= LogManager.getLogger("tradeComparaison");
	private static final Logger loggerTradeBalance= LogManager.getLogger("tradeBalance");
	
	long timeSleeping = 60l * 1000l;
	Thread t = new Thread(this);
	String symbolsCurrenciesSelected;
	boolean isOn = true;
	BitfinexClient bitfinexClient;
	SessionCurrencies sessionCurrencies;
	Config config;
	public ThreadTrading(Config config)  {	
		this.config = config;
		try {			
			this.symbolsCurrenciesSelected = SymbolsConfig.getInstance().getSymbolsSelectedRequest();			
			ProtectedConfigFile pcf  = new ProtectedConfigFile(config.password);
			String key = pcf.get(ProtectedConfigFile.keyBifinexApiKey);
			String keySecret = pcf.get(ProtectedConfigFile.keyBitfinexSecretKey);
			bitfinexClient= new BitfinexClient(key, keySecret);
			t.setDaemon(true);
			t.start();
			
		} catch (Exception e) {
			isOn = false;
			this.timeSleeping=1l;
			awake();
			e.printStackTrace();
		}
	}

	public void run() {
		
			loggerTrade.info( "run  DAILY_CHANGE_PERC	float	Amount that the price has changed expressed in percentage terms");
			while(isOn ){
				try {
					Tickers tickers = fetchTickers();
					Balances balances = fetchBalances();
					ITicker tickerBest = tickers.getlTickersOrdered().get(0);
					if (sessionCurrencies == null){
						sessionCurrencies = new SessionCurrencies(tickers);
					}else {
						sessionCurrencies.update(tickers);
					}
					ITicker zBest = sessionCurrencies.getTickerBest();
						
					loggerTrade.info(""+tickers.toString());
					loggerTradeComparaison.info("Instantane\t|Best:"+tickerBest.getShortName()+"\t|"+getTrace(tickers.getlTickersOrdered()));
					loggerTradeComparaison.info("Filtred   \t|Best:"+zBest.getShortName()+"\t|"+getTrace(sessionCurrencies.getListOrder_byDailyChangePerCent()));
					
					List<Order> orders = balances.process(sessionCurrencies);
					if(this.config.orderAble){
						OrderManager.getInstance().sendOrders(this.bitfinexClient,orders);
					}
					this.sessionCurrencies.setBalancesCurrent(balances);
					MidasGUI.getInstance().updateThread();
				} catch (Exception e) {
					log("Exception "+e.getClass()+" "+e.getMessage());
				}
				sleep(timeSleeping);
			}
			log( "stop thread");
	}
	
	private static DecimalFormat decimalFormat = new DecimalFormat("0.000");
	
	private String getTrace(List<ITicker> list){
		String s ="";
		for(ITicker it : list){
			s+=getTrace(it);
		}
		return s;
	}
	private String getTrace(ITicker x){
		return x.getShortName()+" ["+decimalFormat.format(x.getDaylyChangePerCent())+"]";
	}
	private Tickers fetchTickers(){
		try {
			return   (Tickers) bitfinexClient.serviceProcess(EnumService.tickersV2,"",symbolsCurrenciesSelected);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Balances fetchBalances() throws Exception{
		//return (Balances) bitfinexClient.serviceProcess(EnumService.balances,"",null);
		return   (Balances) bitfinexClient.serviceProcess(EnumService.balances,"",null);

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

	
	
	
}
