package bg.panama.btc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.ActiveOrders;

public class OrderFactory {
  
	
	
	private static OrderFactory instance  = new OrderFactory();
	PanicThreadOrderSaveAllInUsd orderThreadSaveAllInUsd;
	boolean isPanic = false;
	private OrderFactory() {		
	}

	
	public static OrderFactory getInstance() {
		return instance;
	}

	
	public void cancelAllOrders() {
		BitfinexClient bitfinexClient = BitfinexClientFactory.getBitfinexClientAuthenticated();
		cancelAllOrders(bitfinexClient);
	}
	
	public Object cancelAllOrders(BitfinexClient bitfinexClient){
		try {
			Object o = bitfinexClient.serviceProcess(EnumService.cancelAllOrders,"","");
			System.out.println("cancelAllOrders :"+o);
			try {
				Thread.sleep(5000);// Cet ordre se passe en 2 temps. Il est necessiare d'attendre ou les ordres envoyéd dans la foulée sont annullés
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return o;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new  RuntimeException(e);
		}		
	}
	
	public ActiveOrders getAllActivesOrders(){
		try {
			BitfinexClient bitfinexClient = BitfinexClientFactory.getBitfinexClientAuthenticated();
			ActiveOrders activeOrders= (ActiveOrders) bitfinexClient.serviceProcess(EnumService.orders,"","");
			return activeOrders;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
