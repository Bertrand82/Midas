package btc;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import bg.panama.btc.BitfinexClient;
import bg.panama.btc.BitfinexClient.EnumService;
import bg.panama.btc.BitfinexClientFactory;
import bg.panama.btc.model.OrderBook;
import bg.panama.btc.model.OrderBookFactory;

public class BookOrderTest {
	
	public static String passwordDefault = "mypassword";
	 
	
	BitfinexClient bfnx;
	public BookOrderTest() {
		bfnx = BitfinexClientFactory.getBitfinexClient(passwordDefault);
		System.out.println("BitfinexTest.bfnx "+bfnx);
	}
	
	
	@Test
	public void test0() throws Exception{
		OrderBook orderBook = new OrderBook();
		double perCentOfAmount=0.5;
		double priceVente  = orderBook.getPrice(false);
		System.out.println(" -------------------------------------------test----");
		double priceAchat  = orderBook.getPrice(true);
		System.err.println("Price Achat "+priceAchat);
		System.err.println("Price Vente "+priceVente);
		System.err.println("Delta : "+((priceVente - priceAchat)*100/priceAchat));
		assertTrue(priceVente > priceAchat);
	}
	
	//@Test
	public void test1() throws Exception{
		OrderBook orderBook = new OrderBook();
		double perCentOfAmount=0.5;
		double priceVente  = orderBook.getLowerFromList(orderBook.getListAsks(),perCentOfAmount);
		System.out.println(" -------------------------------------------test----");
		double priceAchat  = orderBook.getHigerFromList(orderBook.getListBids(),perCentOfAmount);
		System.err.println("Price Achat "+priceAchat);
		System.err.println("Price Vente "+priceVente);
		System.err.println("Delta : "+((priceVente - priceAchat)*100/priceAchat));
		assertTrue(priceVente > priceAchat);
	}

	
	@Test
	public void test2() throws Exception{
		String symbol = "btcusd";
		OrderBook orderBook = OrderBookFactory.getInstance().getOrderBook(symbol);
		System.out.println("OrderBook ::: "+orderBook);
	}
	

	//@Test
	public  void testBitfinexV1_orderbook() throws Exception {
		Object o = bfnx.serviceProcess(EnumService.orderbook,"","btcusd");
		System.out.println("testBitfinexV1_orderbook "+o);
	}
	

}
