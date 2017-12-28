package btc.trading.first;



import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import btc.BitfinexClient;
import btc.BitfinexClient.EnumService;
import btc.model.v2.ITicker;
import btc.model.v2.Tickers;
import btc.swing.ProtectedConfigFile;
import btc.swing.SymbolsConfig;



public class ThreadTrading implements Runnable{

	private static final Logger loggerTrade= LogManager.getLogger("trade");
	private static final Logger loggerTradeComparaison= LogManager.getLogger("tradeComparaison");
	long timeSleeping = 60l * 1000l;
	Thread t = new Thread(this);
	String symbolsCurrenciesSelected;
	boolean isOn = true;
	BitfinexClient bitfinexClient;
	Z_1_listCurrencies z_1_listCurrencies;
	public ThreadTrading(String password)  {		
		try {			
			this.symbolsCurrenciesSelected = SymbolsConfig.getInstance().getSymbolsSelectedRequest();			
			ProtectedConfigFile pcf  = new ProtectedConfigFile(password);
			String key = pcf.get(ProtectedConfigFile.keyBitfinexSecretKey);
			String keySecret = pcf.get(ProtectedConfigFile.keyBifinexApiKey);
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
					ITicker tickerBest = tickers.getlTickersOrdered().get(0);
					if (z_1_listCurrencies == null){
						z_1_listCurrencies = new Z_1_listCurrencies(tickers);
					}else {
						z_1_listCurrencies.update(tickers);
					}
					ITicker tickerWorse = tickers.getlTickersOrdered().get(tickers.getlTickers().size()-1);
					Z_1_Currency zBest = z_1_listCurrencies.getTickerBest();
					Z_1_Currency zWorse = z_1_listCurrencies.getTickerWorse();
					
					loggerTrade.info(""+tickers.toString());
					loggerTradeComparaison.info("Instantane\t|"+getTrace(tickerBest,tickerWorse));
					loggerTradeComparaison.info("Filtred   \t|"+getTrace(zBest,zWorse));
				} catch (Exception e) {
					log("Exception "+e.getClass()+" "+e.getMessage());
				}
				sleep(timeSleeping);
			}
			log( "stop thread");
		
	}
	private static DecimalFormat decimalFormat = new DecimalFormat("0.000");
	
	private String getTrace(ITicker best, ITicker worse){
		return "Best   \t: "+getTrace(best)+"" +"\t| Worse \t: "+getTrace(worse)+"\t|";
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
	
	
}
