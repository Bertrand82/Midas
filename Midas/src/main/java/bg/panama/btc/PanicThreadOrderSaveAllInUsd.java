package bg.panama.btc;

import java.util.ArrayList;
import java.util.List;

import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.model.Balance;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.operation.Order;
import bg.panama.btc.trading.first.BalancesFactory;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;

public class PanicThreadOrderSaveAllInUsd implements Runnable {

	BitfinexClient bitfinexClient;

	public PanicThreadOrderSaveAllInUsd(BitfinexClient bitfinexClient) {
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
		Balances balances = ServiceCurrencies.getInstance().getBalances();
		try {
			cancelAllOrders();
			SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
			
			List<Order> lOrders = new ArrayList<>();
			for (Balance balance : balances.getlBalancesExchange()) {
				double ammountDollar = balance.getAmountInDollar();
				if (ammountDollar > 50) {
					if (balance.getCurrency().equalsIgnoreCase("usd")){
						
					}else {
					String currency= balance.getCurrency();
					Order order = new Order(currency, balance.getAmount(),Order.Side.sell,Order.TypeChoicePrice.panic);
					balance.addOrderVente(order.getAmmount());
					lOrders.add(order);
					}
				}
			}
			OrderManager.getInstance().sendOrders(bitfinexClient, lOrders);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BalancesFactory.instance.merge(balances);
		}
	}

	public void cancelAllOrders() throws Exception {
		Object o = this.bitfinexClient.serviceProcess(EnumService.cancelAllOrders, "", "");
		System.out.println("orders :" + o);
	}

}
