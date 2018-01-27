package bg.panama.btc;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.OrderBook;
import bg.panama.btc.model.OrderBookFactory;
import bg.panama.btc.model.Symbols;
import bg.panama.btc.model.TickerV1;
import bg.panama.btc.trading.first.Order;
import bg.panama.btc.trading.first.OrderPersistFactory;
import bg.panama.btc.trading.first.OrderRetour;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;
import btc.BookOrderTest;

public class OrderManager {

	private static final Logger loggerOrder = LogManager.getLogger("orders");
	private static OrderManager instance = new OrderManager();


	public static OrderManager getInstance() {
		return instance;
	}

	public void cancelAndSendOrders_(BitfinexClient bfnx, List<Order> orders) {
		SessionCurrencies sc = ServiceCurrencies.getInstance().getSessionCurrencies();
		if (sc == null) {
			return;
		}
		boolean isModePanic = sc.isModePanic();
		System.out.println("cancelAndSendOrders order.size: " + orders.size());
		if (isModePanic) {
			System.out.println("sendOrders MODE PANIC No PRecessiong orders" + orders);
			return;
		}
		if (orders.size() > 0) {
			OrderFactory.getInstance().cancelAllOrders(bfnx);

		}
		sendOrders(bfnx, orders);
		OrderPersistFactory.instance.persists(orders);
	}

	public void sendOrders(BitfinexClient bfnx, List<Order> orders) {
		for (Order order : orders) {
			sendOrderPrivate(bfnx, order);
		}
	}


	public void sendOrderPrivate(BitfinexClient bfnx, Order order)  {
		try {
			
			processPrice(order);	
			if (order.getPrice() <= 0.0000001) throw new Exception("Price Should notbe 0!!!");
			System.err.println("sendOrderPrivateA order :"+order);
			loggerOrder.info("sendOrderWithPrice : order :" + order);
			boolean fireOk = true;
			String r = "";
			if (fireOk) {
				System.err.println("sendOrderPrivateB sending order :" + order);
				JSONObject jo = bfnx.sendOrder(order);
				System.err.println("sendOrderPrivateC sending order retour:" + jo);
				Object orderRetour = instancie(jo, OrderRetour.class);
				System.err.println("OrderRetour "+orderRetour);
				
			} else {
				r = "No SEND ORDER  It is ONLY simu  !!!!! ";
			}
			loggerOrder.info("reponse Order " + r);
			System.err.println("sendOrderPrivateD retour :" + r);
		} catch (Exception e) {
			loggerOrder.error("Exception44 sendOrderBB Exception : ", e);
			System.err.println("Exception44 ");
			e.printStackTrace();
		}

	}

	private void  processPrice(Order order) throws Exception {
		String symbol=order.getCurrency();
		Order.TypeChoicePrice choice = order.getTypeChoicePrice();
		boolean achat = order.isBuying();
		double price;
		switch (choice) {
		case manual:
			return;			
		case fromBookOrder:
			price = getPriceFromBookOrder(symbol, achat);
			break;
		case fromTickers:
		case panic:
		default:
			price = getPriceFromTickers(symbol, achat);
		}
		System.err.println("Set Price "+price);
		order.setPrice(price);
		return ;
	}

	private double getPriceFromBookOrder(String symbol, boolean achat) throws Exception{
			OrderBook orderBook = OrderBookFactory.getInstance().getOrderBook(symbol);
			double price = orderBook.getPrice(achat);
			return price;
		
		
	}

	private double getPriceFromTickers(String symbol, boolean achat) throws Exception {
		BitfinexClient bfnx = BitfinexClientFactory.getBitfinexClientAuthenticated();
		TickerV1 ticker = (TickerV1) bfnx.serviceProcess(EnumService.ticker, "", symbol);
		System.err.println("getPriceFromTickers LastPrice : " + symbol + "  lastPrice " + ticker.getLast_price()
				+ "   MidPrice :  " + ticker.getMid() + "  ");
		double price;
		if (achat) {
			price = Math.min(ticker.getLast_price(), ticker.getMid());
		} else {
			price = Math.max(ticker.getLast_price(), ticker.getMid());
		}
		return price;
	}

	public void sendOrderPrivate(Order order) {
		BitfinexClient bfx  = BitfinexClientFactory.getBitfinexClientAuthenticated();
		sendOrderPrivate(bfx,order);
	}
	
	private static  Object instancie(JSONObject jo, Class clazz) throws Exception {
		if (jo == null) {
			return null;
		}
		if (clazz == null) {
			return jo;
		}
		Constructor<?> c = clazz.getConstructor(JSONObject.class);
		Object o = c.newInstance(jo);
		return o;
	}

}
