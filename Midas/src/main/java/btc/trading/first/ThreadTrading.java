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
	private boolean emergencySaveAllRequest = false;
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
	Balances balances;
	public void run() {
		
			loggerTrade.info( "run  DAILY_CHANGE_PERC	float	Amount that the price has changed expressed in percentage terms");
			while(isOn ){
				try {
					Tickers tickers = fetchTickers();
					Balances balances = fetchBalances();
					
					if (sessionCurrencies == null){
						sessionCurrencies = new SessionCurrencies(tickers);
					}else {
						sessionCurrencies.update(tickers);
					}
					ITicker zBest = sessionCurrencies.getTickerBest();
						
					loggerTrade.info(""+tickers.toString());
					loggerTradeComparaison.info("Instantane\t|Best:"+zBest.getShortName()+"\t|"+getTrace(tickers.getlTickers()));
					loggerTradeComparaison.info("Filtred   \t|Best:"+zBest.getShortName()+"\t|"+getTrace2(sessionCurrencies.getListOrder_byHourlyChangePerCentByDay()));
					
					List<Order> orders = balances.process(sessionCurrencies);
					if(this.config.orderAble){
						OrderManager.getInstance().sendOrders(this.bitfinexClient,orders);
					}
					this.sessionCurrencies.setBalancesCurrent(balances);
					MidasGUI.getInstance().updateThread();
				} catch (Exception e) {
					log("Exception22: "+e.getClass()+" "+e.getMessage());
				}
				checkEmergencySaveRequest();
				sleep(timeSleeping);
				checkEmergencySaveRequest();
			}
			log( "stop thread");
	}
	
	



	private void checkEmergencySaveRequest(){
		if (emergencySaveAllRequest){
			List<Order> orders = sessionCurrencies.saveAllInDollar();
			log("emergencySaveAll orders.size :"+orders.size());
			log("emergencySaveAll orders.size :"+orders);
			if(this.config.orderAble){
				OrderManager.getInstance().sendOrders(this.bitfinexClient,orders);
			}else{
				log("Warning Save All in Dollar Order put noOrderable");
			}
		}
		emergencySaveAllRequest=false;
	}
	
	

	private static DecimalFormat decimalFormat = new DecimalFormat("0.000");
	

	private String getTrace2(List<SessionCurrency> list) {
		String s ="";
		for(SessionCurrency it : list){
			s+=getTrace(it);
		}
		return s;
	}

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

	public void emergencySave(String from) {
		log("emergy save request from "+from);
		this.emergencySaveAllRequest=true;
		awake();
	}

	public Config getConfig() {
		return config;
	}

	
	
	
}
