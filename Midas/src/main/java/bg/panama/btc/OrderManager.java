package bg.panama.btc;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.OrderBook;
import bg.panama.btc.model.OrderBookFactory;
import bg.panama.btc.model.Symbols;
import bg.panama.btc.model.TickerV1;
import bg.panama.btc.trading.first.Order;
import bg.panama.btc.trading.first.OrderPersistFactory;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;
import btc.BookOrderTest;

public class OrderManager {

	private static final Logger loggerOrder = LogManager.getLogger("orders");
	private static OrderManager instance = new OrderManager();

	enum ChoicePriceOrder {
		panic, fromTickers, fromBookOrder
	}

	public static OrderManager getInstance() {
		return instance;
	}

	public void cancelAndSendOrders(BitfinexClient bfnx, List<Order> orders) {
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

	protected void sendOrders(BitfinexClient bfnx, List<Order> orders) {
		for (Order order : orders) {
			sendOrder(bfnx, order);
		}
	}

	public void sendOrder(BitfinexClient bfnx, Order order) {
		try {
			sendOrderPrivate(bfnx, order);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendOrderPrivate(BitfinexClient bfnx, Order order) throws ExceptionNoSymbolForOrder {
		try {

			Symbols symbols = (Symbols) bfnx.serviceProcess(EnumService.symbols, null, null);
			String symbol;

			if (symbols.contains(order.getSymbol())) {

				symbol = order.getSymbol();

			} else {
				loggerOrder.warn("No symbol for order" + order + " try to convert in btc!!");
				throw new Exception("No symbol for order " + order);
			}

			ChoicePriceOrder choicePriceOrder = ChoicePriceOrder.fromTickers;
			double price = getPrice(symbol, choicePriceOrder, order.isBuying());
			order.setPrice(price);
			
			loggerOrder.info("sendOrder : " + order);
			boolean fireOk = true;
			String r = "";
			if (fireOk) {
				System.err.println("sendOrderPrivate sending order :" + order);
				r = bfnx.sendOrder(order);
			} else {
				r = "No SEND ORDER  It is ONLY simu  !!!!!";
			}
			loggerOrder.info("reponse Order " + r);
			System.err.println("sendOrderPrivate retour :" + r);
		} catch (Exception e) {
			loggerOrder.error("Exception44 sendOrderBB Exception : ", e);
			System.err.println("Exception44 ");
			e.printStackTrace();
		}

	}

	private double getPrice(String symbol, ChoicePriceOrder choice, boolean achat) throws Exception {
		double price;
		switch (choice) {

		case fromBookOrder:
			price = getPriceFromBookOrder(symbol, achat);
			break;
		case fromTickers:
		case panic:
		default:
			price = getPriceFromTickers(symbol, achat);
		}

		return price;
	}

	private double getPriceFromBookOrder(String symbol, boolean achat) {
		try {
			OrderBook orderBook = OrderBookFactory.getInstance().getOrderBook(symbol);
			double price = orderBook.getPrice(achat);
			return price;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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

}
