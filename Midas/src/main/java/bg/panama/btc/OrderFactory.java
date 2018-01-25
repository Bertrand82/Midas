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
			BitfinexClient bitfinexClient = BitfinexClientFactory.getBitfinexClientAuthenticated();
			loggerOrder.info("emergencySaveInDollar from :"+from);
			if (this.orderThreadSaveAllInUsd == null){
				this.orderThreadSaveAllInUsd = new OrderThreadSaveAllInUsd(bitfinexClient);
			}
			this.orderThreadSaveAllInUsd.saveAllInUSD("from OrderFactory from "+from);
	}
	
	
	public Object cancelAllOrders(BitfinexClient bitfinexClient){
		try {
			Object o = bitfinexClient.serviceProcess(EnumService.cancelAllOrders,"","");
			System.out.println("cancelAllOrders :"+o);
			return o;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new  RuntimeException(e);
		}
		
	}
	
}
