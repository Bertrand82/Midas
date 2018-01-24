package bg.panama.btc;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.Symbols;
import bg.panama.btc.model.TickerV1;
import bg.panama.btc.trading.first.Order;



public class OrderManager {

	private static final Logger loggerOrder= LogManager.getLogger("orders");
	private static OrderManager instance = new OrderManager();
	
	public static OrderManager getInstance() {
		return instance;
	}
	
	public void sendOrdersAchat (BitfinexClient bfnx ,List<Order> orders){
		for(Order order: orders){
			System.out.println("sendOrdersAchat No Implemented yet Order"+order);
		
		}
	}
	
	public void sendOrdersVente(BitfinexClient bitfinexClient, List<Order> orders) {
		for(Order order: orders){
			System.out.println("sendOrdersVente No Implemented yet Order"+order);		
		}
	}



	public  void sendOrder(BitfinexClient bfnx ,Order order){
		try {
			try {
				sendOrderPrivate(bfnx, order);
			} catch (ExceptionNoSymbolForOrder e) {
				// Je converti en usd qui est convertible en tout apparament
				order.setCurrencyTo("usd");
				try {
					sendOrderPrivate(bfnx, order);
				} catch (ExceptionNoSymbolForOrder e1) {	
					System.err.println("Deuxiemme echec for order !!!");
					loggerOrder.warn("Desesperant!");
				}
			}
		} catch (Throwable e) {
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
			double price = getPrice(symbol);
			order.setPrice(price);
			if(order.isBuying()){
				double amountToConvert = order.getAmountDesired();
				order.setAmmountToConvert(amountToConvert);
			}
			loggerOrder.info("sendOrder : "+order);
			
			//String r = bfnx.sendOrder(order);
			String r ="No SEND ORDER  IMPLEMENTED YET";
			loggerOrder.info("reponse Order "+r);
		} catch (ExceptionNoSymbolForOrder e) {
			throw e;
		} catch (Exception e) {
			loggerOrder.error("Exception44 sendOrderBB Exception : ",e);
			System.err.println("Exception44 ");
			e.printStackTrace();			
		}

	}

	private double getPrice(String symbol) throws Exception {
		double price = getPriceFromTickers(symbol);
		return price;
	}
	private double getPriceFromTickers(String symbol) throws Exception {
		BitfinexClient bfnx = BitfinexClientFactory.getBitfinexClientAuthenticated();
		TickerV1 ticker =(TickerV1) bfnx.serviceProcess(EnumService.ticker, "", symbol);
		System.err.println("LastPrice : "+ticker.getLast_price()+"   MidPrice :  "+ticker.getMid());
		double price = Math.max(ticker.getLast_price(), ticker.getMid());
		return price;
	}

	
}
