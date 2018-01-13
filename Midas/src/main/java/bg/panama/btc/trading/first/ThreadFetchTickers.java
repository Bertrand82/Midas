package bg.panama.btc.trading.first;



import java.text.DecimalFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.OrderManager;
import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.ActiveOrder;
import bg.panama.btc.model.ActiveOrders;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.v2.ITicker;
import bg.panama.btc.model.v2.Tickers;
import bg.panama.btc.model.v2.TickersFactory;
import bg.panama.btc.swing.MidasGUI;
import bg.panama.btc.swing.ConfigFileProtected;
import bg.panama.btc.swing.SymbolsConfig;



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
	Config config;
	private boolean emergencySaveAllRequest = false;
	public ThreadFetchTickers(Config config)  {	
		this.config = config;
		try {			
			this.symbolsCurrenciesSelected = SymbolsConfig.getInstance().getSymbolsSelectedRequest();
			String pwd = config.getPassword();
			ConfigFileProtected pcf  = new ConfigFileProtected(pwd);
			//String key = pcf.get(ProtectedConfigFile.keyBifinexApiKey);
			//String keySecret = pcf.get(ProtectedConfigFile.keyBitfinexSecretKey);
			//bitfinexClient= new BitfinexClient(key, keySecret);
			bitfinexClient= new BitfinexClient();
			t.setDaemon(true);
			t.start();
			
		} catch (Exception e) {
			isOn = false;
			this.timeSleeping=1l;
			awake();
			e.printStackTrace();
		}
	}
	Balances balances_;
	TickersFactory tickersFactory = TickersFactory.getInstance();
	public void run() {
		
			loggerTrade.info( "run  DAILY_CHANGE_PERC	float	Amount that the price has changed expressed in percentage terms");
			while(isOn ){
				try {
					Tickers tickers = fetchTickers();
					ServiceCurrencies.getInstance().setTickersCurrent(tickers);
					//Balances balances_ = fetchBalances();
					
					if (sessionCurrencies == null){
						sessionCurrencies = new SessionCurrencies(tickers);
					}else {
						sessionCurrencies.update(tickers);
					}
					ITicker zBest = sessionCurrencies.getTickerBest();
						
					loggerTrade.info(""+tickers.toString());
					loggerTradeComparaison.info("Instantane\t|Best:"+zBest.getShortName()+"\t|"+getTrace( tickers.getlTickers()));
					loggerTradeComparaison.info("Filtred   \t|Best:"+zBest.getShortName()+"\t|"+getTrace2(sessionCurrencies.getListOrder_byHourlyChangePerCentByDay()));
					
					//List<Order> orders = balances.process(sessionCurrencies);
					if(this.config.isOrderAble()){
						//OrderManager.getInstance().sendOrders(this.bitfinexClient,orders);
					}
					//this.sessionCurrencies.setBalancesCurrent(balances);
					MidasGUI.getInstance().updateThread();
					tickersFactory.persists(tickers);
				} catch (Exception e) {
					log("Exception22: "+e.getClass()+" "+e.getMessage());
					System.err.println("Exception22 "+e.getClass()+" "+e.getMessage());
					e.printStackTrace();
				} catch (Throwable e) {
					log("Exception23: "+e.getClass()+" "+e.getMessage());
					e.printStackTrace();
				}
			//	checkEmergencySaveRequest();
				sleep(timeSleeping);
			//	checkEmergencySaveRequest();
			}
			log( "stop thread");
	}
	
	



	private void checkEmergencySaveRequest() {
		try {
			this.cancelAllOrders();
			if (emergencySaveAllRequest){
				List<Order> orders = sessionCurrencies.saveAllInDollar();
				log("ThreadTrading.emergencySaveAll orders.size :"+orders.size());
				log("ThreadTrading.emergencySaveAll orders.size :"+orders);
			
				OrderManager.getInstance().sendOrders(this.bitfinexClient,orders);
				
			}
			emergencySaveAllRequest=false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	private static DecimalFormat decimalFormat = new DecimalFormat("0.000");
	

	private String getTrace2(List<SessionCurrency> list) {
		String s ="";
		for(SessionCurrency it : list){
			s+=getTrace(it);
		}
		return s;
	}

	private String getTrace(List<? extends ITicker> list){
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
