package bg.panama.btc;

import java.util.ArrayList;
import java.util.List;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.Balance;
import bg.panama.btc.model.Balances;
import bg.panama.btc.trading.first.Order;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;

public class OrderThreadSaveAllInUsd implements Runnable {

	BitfinexClient bitfinexClient;

	public OrderThreadSaveAllInUsd(BitfinexClient bitfinexClient) {
		this.bitfinexClient = bitfinexClient;
	}

	public void saveAllInUSD(String from) {
		System.err.println("SaveAllInUSD from : " + from);
		Thread t = new Thread(this);
		t.setDaemon(false);
		t.start();
	}

	@Override
	public void run() {
		try {
			cancelAllOrders();
			SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
			Balances balances = ServiceCurrencies.getInstance().getBalances();
			List<Order> lOrders = new ArrayList<>();
			for (Balance balance : balances.getlBalancesExchange()) {
				double ammountDollar = balance.getAmountInDollar();
				if (ammountDollar > 50) {
					String currencyFrom = balance.getCurrency();
					Order order = new Order(currencyFrom, "usd", balance.getAmount());
					lOrders.add(order);
				}
			}
			OrderManager.getInstance().sendOrders(bitfinexClient, lOrders);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelAllOrders() throws Exception {
		Object o = this.bitfinexClient.serviceProcess(EnumService.cancelAllOrders, "", "");
		System.out.println("orders :" + o);
	}

}
