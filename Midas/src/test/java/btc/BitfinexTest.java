package btc;

import static org.junit.Assert.*;

import org.junit.Test;

import btc.BitfinexClient.EnumService;

public class BitfinexTest {

	@Test
	public void test() throws Exception{
		testBitfinex();
	}

	
	public static void testBitfinex() throws Exception {
		String apiKey = "egerM4sGU0jYYTdab8ViADYBr0dHIbAotwY5byr44T1";
		String apiKeySecret = "XfHGunq6dFkr8Xz3IlEstuj4c3000fHGtTg5jeo2FQo";
		for(EnumService es : EnumService.values()){
			System.out.println(es+" ::::: "+es.key);
		}
		BitfinexClient bfnx = new BitfinexClient(apiKey, apiKeySecret);
		bfnx.serviceProcessV1(EnumService.balances);
		bfnx.serviceProcessV1(EnumService.account_infos);
		bfnx.serviceProcessV1(EnumService.summary);
		//bfnx.serviceProcess(services.deposit_new);
		bfnx.serviceProcessV1(EnumService.key_info);
		bfnx.serviceProcessV1(EnumService.margin_infos);
		bfnx.serviceProcessV1(EnumService.transfer);
		bfnx.serviceProcessV1(EnumService.withdraw);
		System.out.println("------------------------------------------ public --------------");
		bfnx.serviceProcessV1(EnumService.symbols);
		bfnx.serviceProcessV1(EnumService.symbols_details);
		bfnx.serviceProcess(EnumService.ticker,"","btcusd");
		bfnx.serviceProcess(EnumService.stats,"","btcusd");
		bfnx.serviceProcess(EnumService.orderbook,"","btcusd");
		bfnx.serviceProcess(EnumService.trades,"","btcusd");
		bfnx.serviceProcess(EnumService.lends,"usd","");
		bfnx.serviceProcessV1(EnumService.statusV2);
		bfnx.serviceProcess(EnumService.tickerV2,"","tBTCUSD");
		bfnx.serviceProcess(EnumService.tickersV2,"","tBTCUSD,tLTCUSD,fUSD");
		bfnx.serviceProcess(EnumService.tradesV2,"","tBTCUSD");
		bfnx.serviceProcess(EnumService.walletV2,"","");
			
	}

}
