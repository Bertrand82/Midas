package btc;

import static btc.UtilBtc.df;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import btc.BitfinexClient.EnumService;
import btc.model.Symbols;
import btc.model.TickerV1;
import btc.trading.first.Order;

public class OrderManager {

	private static final Logger loggerOrder= LogManager.getLogger("orders");
	private static OrderManager instance = new OrderManager();
	
	public static OrderManager getInstance() {
		return instance;
	}
	
	public void sendOrders (BitfinexClient bfnx ,List<Order> orders){
		for(Order order: orders){
			sendOrder(bfnx, order);
		}
	}

	public  void sendOrder(BitfinexClient bfnx ,Order order){
		try {
			try {
				System.err.println("AAAAAAAAAAAAAAAAAa");
				sendOrderPrivate(bfnx, order);
			} catch (ExceptionNoSymbolForOrder e) {
				System.err.println("BBBBBBBBBBBBBBBBBB");
				// Je converti en btc qui est convertible en tout apparament
				System.err.println("Conversion en btc");
				order.setCurrencyTo("btc");
				try {
					sendOrderPrivate(bfnx, order);
				} catch (ExceptionNoSymbolForOrder e1) {	
					System.err.println("Deuxiemme echec for order !!!");
					loggerOrder.warn("Desesperant! Essayer le usd ?");
				}
			}
		} catch (Throwable e) {
			System.err.println("cccccccccccccccccccc");
			e.printStackTrace();
		}
	}
	
	public  void sendOrderPrivate(BitfinexClient bfnx ,Order order) throws ExceptionNoSymbolForOrder{
		try {
			
			Symbols symbols = (Symbols) bfnx.serviceProcess(EnumService.symbols, null, null);
			String symbol;
			
			if (symbols.contains(order.getSymbol())) {
				
				symbol = order.getSymbol();
				
				order.setSide(Order.side_sell);
			    order.setAmmountToConvert(order.getAmmount());
			} else if (symbols.contains(order.getSymbolInvers())) {
				
				symbol = order.getSymbolInvers();	
				order.setSide(Order.side_buy);
			} else {
				loggerOrder.warn("No symbol for order" + order+" try to convert in btc!!");
				throw new ExceptionNoSymbolForOrder("No symbol for order " + order);
			}
			order.setSymbolWithDirection(symbol);
			TickerV1 ticker =(TickerV1) bfnx.serviceProcess(EnumService.ticker, "", symbol);
			order.setPrice(ticker.getLast_price());
			if(order.isBuying()){
				double amountToConvert = order.getAmountDesired();
				order.setAmmountToConvert(amountToConvert);
			}
			loggerOrder.info("sendOrder : "+order);
			
			String r = bfnx.sendOrder(order);
			loggerOrder.info(r);
		} catch (ExceptionNoSymbolForOrder e) {
			throw e;
		} catch (Exception e) {
			loggerOrder.error("Exception44 sendOrderBB Exception : ",e);
			System.err.println("Exception44 ");
			e.printStackTrace();			
		}

	}


}
