package bg.panama.btc.model;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.BitfinexClientFactory;

public class OrderBookFactory {

	
	private static OrderBookFactory instance = new OrderBookFactory();

	public static OrderBookFactory getInstance() {
		return instance;
	}
	/**
	 * 
	 * @param symbol ex : "btcusd"
	 * @return
	 */
	public OrderBook getOrderBook(String symbol) throws Exception{
		BitfinexClient bfnx = BitfinexClientFactory.getBitfinexClientFree();
		Object o = bfnx.serviceProcess(EnumService.orderbook,"",symbol);
		OrderBook orderBook = (OrderBook) o;
		return orderBook;
		
	}
}
