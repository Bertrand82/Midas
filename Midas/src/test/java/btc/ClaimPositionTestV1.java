package btc;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import btc.BitfinexClient.EnumService;
import btc.model.Balances;
import btc.model.Symbols;
import btc.model.TickerV1;
import btc.swing.SymbolsConfig;
import btc.trading.first.Order;
import static btc.UtilBtc.*

;public class ClaimPositionTestV1 {

	BitfinexClient bfnx;

	public ClaimPositionTestV1() {
		bfnx = BitfinexClientFactory.getBitfinexClient(BitfinexTest.passwordDefault);
	}

	
	/*
	 * // view how to authenticate here: //
	 * https://docs.bitfinex.com/v1/docs/rest-auth
	 * 
	 * var payload = { 
	 * request: '/v1/order/new', 
	 * nonce: Date.now().toString(),
	 * symbol: 'BTCUSD', 
	 * amount: '0.3', 
	 * price: '1000', 
	 * exchange: 'bitfinex',
	 * side: 'sell', 
	 * type: 'exchange market' }
	 * 
	 * // or use bitfinex-api-node
	 * 
	 * const BFX = require('bitfinex-api-node') 
	 * const bfxRest = new BFX(apiKey,
	 * apiSecretKey, {version: 1}).rest bfxRest.new_order('BTCUSD', '1.269',
	 * '1000', 'bitfinex', 'sell', 'exchange market', (err, res) => { if (err)
	 * console.log(err) console.log(result) })
	 */
	@Test
	public void testNewOrder() throws Exception {
		Order order = new Order("btc", "XRP", 0.003);
		//Order order = new Order("xrp", "btc", 15);
		// ATTENTION avant de tester: Cela passe un ordre
		
	
	}
	/*
	 * 
///////////////////////////////////////////////////////////////////

// view how to authenticate here:
// https://docs.bitfinex.com/v1/docs/rest-auth

var payload = {
   request: '/v1/positions',
   nonce: Date.now().toString(),
}
///////////////////////////////////////////////////////////////////////////////

*/
	
	@Test
	public  void testBitfinexV1_positions() throws Exception {
	Object o = bfnx.serviceProcessV1(EnumService.positions);
	System.out.println("Positions :"+o);
	}

	
}
