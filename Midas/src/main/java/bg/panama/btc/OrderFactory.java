package bg.panama.btc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.BitfinexClient.EnumService;

public class OrderFactory {
  
	private static final Logger loggerOrder= LogManager.getLogger("orders");
	
	
	private static OrderFactory instance  = new OrderFactory();
	OrderThreadSaveAllInUsd orderThreadSaveAllInUsd;
	boolean isPanic = false;
	private OrderFactory() {		
	}

	
	public static OrderFactory getInstance() {
		return instance;
	}

	public void emergencySaveInDollar(String from) {
			this.isPanic = true;
			BitfinexClient bitfinexClient = BitfinexClientFactory.getBitfinexClient();
			loggerOrder.info("emergencySaveInDollar from :"+from);
			if (this.orderThreadSaveAllInUsd == null){
				this.orderThreadSaveAllInUsd = new OrderThreadSaveAllInUsd(bitfinexClient);
			}
			this.orderThreadSaveAllInUsd.saveAllInUSD("from OrderFactory from "+from);
		
	
		
	}
	
	
	public void cancelAllOrders(BitfinexClient bitfinexClient) throws Exception{
		Object o = bitfinexClient.serviceProcess(EnumService.cancelAllOrders,"","");
		System.out.println("cancelAllOrders :"+o);
		
	}
	
}
