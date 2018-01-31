package btc;

import org.junit.Test;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.BitfinexClientFactory;
import bg.panama.btc.model.operation.Order;


public class OrderTestV1 {

	BitfinexClient bfnx;

	public OrderTestV1() {
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
		Order order = new Order("xrp",  0.003,Order.Side.buy,Order.TypeChoicePrice.fromBookOrder);
		//Order order = new Order("xrp", "btc", 15);
		// ATTENTION avant de tester: Cela passe un ordre
		System.err.println("TTENTION avant de tester: Cela passe un ordre");
		//OrderManager.sendOrder(this.bfnx,order);
		
	}
	/*
	 * 
///////////////////////////////////////////////////////////////////

{"request":"/v1/order/new",
"symbol":"xrpbtc",
"amount":"0.06411078",
"side":"sell",
"price":"0.00015598",
"exchange":"bitfinex",
"type":"exchange market",
"nonce":"1514630227809"}
{"message":"Invalid order: minimum size for XRP/BTC is 14.0"}

///////////////////////////////////////////////////////////////////////////////

paylod 
{"request":"/v1/order/new",
"symbol":"xrpbtc",
"amount":"0",
"side":"buy",
"price":"0.00016136",
"exchange":"bitfinex",
"type":"exchange market",
"nonce":"1514631803656"
}
{"message":"Order amount must be positive."}
 
///////////////////////////////////////////////////////////////////////////////////////
{"request":"/v1/order/new",
"symbol":"xrpbtc",
"amount":"0.003",
"side":"buy",
"price":"0.00016144",
"exchange":"bitfinex",
"type":"exchange market",
"nonce":"1514645819454"}
 {"message":"Invalid order: minimum size for XRP/BTC is 14.0"}	
	
	
	/////////////////////////////////////////////////////////////////////
	 * 
	*/


}
