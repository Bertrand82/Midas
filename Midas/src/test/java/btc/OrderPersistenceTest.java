package btc;

import org.junit.Test;

import bg.panama.btc.trading.first.Order;
import bg.panama.btc.trading.first.OrderPersistFactory;

public class OrderPersistenceTest {

	
	@Test
	public void testNewOrder() throws Exception {
		try {
			
			Order order = new Order("xrp", 123,Order.Side.buy, Order.TypeChoicePrice.fromBookOrder);
			OrderPersistFactory.instance.persists(order);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
}
