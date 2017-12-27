package btc.trading.first;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import btc.BitfinexClient;
import btc.BitfinexClient.EnumService;
import btc.model.v2.Tickers;
import btc.swing.ProtectedConfigFile;
import btc.swing.SymbolConfig;
import btc.swing.SymbolsConfig;

public class ThreadTrading implements Runnable{

	private static final Logger logger = LogManager.getLogger("trade");
	long timeSleeping = 60l * 1000l;
	Thread t = new Thread(this);
	String symbolsCurrenciesSelected;
	boolean isOn = true;
	BitfinexClient bitfinexClient;
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
		System.out.println("run ....");
		while(isOn ){
			Tickers tickers = fetchTickers();
			logger.info(""+tickers);
			sleep(timeSleeping);
		}
		logger.info( "stop thread");
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
		logger.info( "stop request from "+from);
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
}
