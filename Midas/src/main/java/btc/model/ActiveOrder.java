package btc.model;

import org.json.JSONObject;
/*
 * 
 [{
  "id":448411365,
  "symbol":"btcusd",
  "exchange":"bitfinex",
  "price":"0.02",
  "avg_execution_price":"0.0",
  "side":"buy",
  "type":"exchange limit",
  "timestamp":"1444276597.0",
  "is_live":true,
  "is_cancelled":false,
  "is_hidden":false,
  "was_forced":false,
  "original_amount":"0.02",
  "remaining_amount":"0.02",
  "executed_amount":"0.0"
}]
 
 
 
 
 */
public class ActiveOrder {
	
	
		  String id ;
		  String symbol;
		  String exchange;
		  Double price;
		  Double avg_execution_price;
		  String side; 
		  String type;
		  String timestamp;
		  Boolean is_live;
		  Boolean is_cancelled;
		  Boolean is_hidden;
		  Boolean was_forced;
		  Double original_amount;
		  Double remaining_amount;
		  Double executed_amount;
		
	
/**
* @param jsonO
 * @throws Exception
 */
	public ActiveOrder(JSONObject jsonO) throws Exception{
		
		this.id = jsonO.getString("id");
		this.symbol = jsonO.getString("symbol");
		this.exchange = jsonO.getString("exchange");
		
		this.price= jsonO.getDouble("price");
		this.avg_execution_price = jsonO.getDouble("avg_execution_price");
		this.side =jsonO.getString("side");
		this.type = jsonO.getString("type");
		timestamp =  jsonO.getString("timestamp");
		is_live =  jsonO.getBoolean("is_live") ;
		is_cancelled =  jsonO.getBoolean("is_cancelled") ;
		is_hidden = jsonO.getBoolean("is_hidden") ;
		was_forced = jsonO.getBoolean("was_forced") ;
		original_amount= jsonO.getDouble("original_amount");
		remaining_amount=jsonO.getDouble("remaining_amount");
		executed_amount=jsonO.getDouble("executed_amount");
		
	}


	@Override
	public String toString() {
		return "order id:"+this.id+"  "+this.side+"  "+original_amount+"  "+symbol+"  "+this.type;
	}

}
