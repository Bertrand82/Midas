package btc;

import java.io.File;

import org.junit.Test;

import btc.BitfinexClient.EnumService;
import btc.model.Balances;
import btc.swing.ProtectedConfigFile;

public class BitfinexTest {
	
	public static String passwordDefault = "mypassword";
	
	
	BitfinexClient bfnx;
	public BitfinexTest() {
		bfnx = BitfinexClientFactory.getBitfinexClient(passwordDefault);
	}
	
	
	@Test
	public void test() throws Exception{
		
	}
	
	@Test
	public void testBitfinexV1_Balance() throws Exception{
		
			//bfnx.serviceProcessV1(EnumService.balances);
			Balances balances =   (Balances) bfnx.serviceProcess(EnumService.balances,"",null);

		System.out.println("Balances :"+balances);
	}

	@Test
	public  void testBitfinexV1_AccountInfo() throws Exception {
		bfnx.serviceProcessV1(EnumService.account_infos);
	}
	
	@Test
	public  void testBitfinexV1_summary() throws Exception {
		bfnx.serviceProcessV1(EnumService.summary);
	}

	@Test
	public  void testBitfinexV1_key_info() throws Exception {
		bfnx.serviceProcessV1(EnumService.key_info);
	}
	
	@Test
	public  void testBitfinexV1_margin_infos() throws Exception {
		bfnx.serviceProcessV1(EnumService.margin_infos);
	}

	@Test
	public  void testBitfinexV1_transfer() throws Exception {
		bfnx.serviceProcessV1(EnumService.transfer);
	}

	@Test
	public  void testBitfinexV1_withdraw() throws Exception {
		bfnx.serviceProcessV1(EnumService.withdraw);
	}

	@Test
	public  void testBitfinexV1_symbols() throws Exception {
		bfnx.serviceProcessV1(EnumService.symbols);
	}

	@Test
	public  void testBitfinexV1_symbols_details() throws Exception {
		bfnx.serviceProcessV1(EnumService.symbols_details);
	}

	@Test
	public  void testBitfinexV1_ticker() throws Exception {
		bfnx.serviceProcess(EnumService.ticker,"","btcusd");
	}
	@Test
	public  void testBitfinexV1_stats() throws Exception {
		bfnx.serviceProcess(EnumService.stats,"","btcusd");
	}
	@Test
	public  void testBitfinexV1_orderbook() throws Exception {
		bfnx.serviceProcess(EnumService.orderbook,"","btcusd");
	}
	@Test
	public  void testBitfinexV1_trades() throws Exception {
		bfnx.serviceProcess(EnumService.trades,"","btcusd");
	}

	@Test
	public  void testBitfinexV1_lends() throws Exception {
		bfnx.serviceProcess(EnumService.lends,"usd","");
	}

	@Test
	public  void testBitfinexV1_statusV2() throws Exception {
		bfnx.serviceProcessV1(EnumService.statusV2);
	}

	@Test
	public  void testBitfinexV1_tickerV2() throws Exception {
		bfnx.serviceProcess(EnumService.tickerV2,"","tBTCUSD");
	}
	
	@Test
	public  void testBitfinexV1_tradesV2() throws Exception {
		bfnx.serviceProcess(EnumService.tradesV2,"","tBTCUSD");
	}

	@Test
	public  void testBitfinexV1_walletV2() throws Exception {
		Object o = bfnx.serviceProcess(EnumService.walletV2,"","");
	}

	

}
