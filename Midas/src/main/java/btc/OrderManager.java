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
				throw new Exception("No simbol for order" + order);
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
		} catch (Exception e) {
			loggerOrder.error("sendOrder Exception : ",e);
			e.printStackTrace();
			
		}

	}


}
