package btc.trading.first;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import btc.BitfinexClient;
import btc.BitfinexClient.EnumService;
import btc.model.v2.Tickers;
import btc.swing.ProtectedConfigFile;
import btc.swing.SymbolConfig;
import btc.swing.SymbolsConfig;

public class ThreadTrading implements Runnable{

	Logger logger = Logger.getLogger("LoggerTrading");  
	long timeSleeping = 60l * 1000l;
	Thread t = new Thread(this);
	String symbolsCurrenciesSelected;
	boolean isOn = true;
	BitfinexClient bitfinexClient;
	public ThreadTrading(String password)  {
		
		try {
			this.symbolsCurrenciesSelected = SymbolsConfig.getInstance().getSymbolsSelectedRequest();
			FileHandler fileLogHandler = new FileHandler("LogTrading.log");
			logger.addHandler(fileLogHandler);
			logger.setLevel(Level.INFO);
			logger.log(Level.INFO, "start thread");
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
		while(isOn ){
			Tickers tickers = fetchTickers();
			sleep(timeSleeping);
		}
		logger.log(Level.INFO, "stop thread");
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
		logger.log(Level.INFO, "stop request from "+from);
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
