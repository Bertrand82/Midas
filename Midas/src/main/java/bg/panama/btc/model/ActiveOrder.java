package bg.panama.btc.model;

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
		return "order id:"+this.id+" side: "+this.side+"  "+original_amount+"  "+symbol+"  type: "+this.type+"  price  "+price;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public String getSide() {
		return side;
	}


	public void setSide(String side) {
		this.side = side;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Boolean getIs_live() {
		return is_live;
	}


	public void setIs_live(Boolean is_live) {
		this.is_live = is_live;
	}


	public Boolean getIs_cancelled() {
		return is_cancelled;
	}


	public void setIs_cancelled(Boolean is_cancelled) {
		this.is_cancelled = is_cancelled;
	}


	public Boolean getIs_hidden() {
		return is_hidden;
	}


	public void setIs_hidden(Boolean is_hidden) {
		this.is_hidden = is_hidden;
	}


	public Double getOriginal_amount() {
		return original_amount;
	}


	public void setOriginal_amount(Double original_amount) {
		this.original_amount = original_amount;
	}


	public Double getRemaining_amount() {
		return remaining_amount;
	}


	public void setRemaining_amount(Double remaining_amount) {
		this.remaining_amount = remaining_amount;
	}


	public Double getExecuted_amount() {
		return executed_amount;
	}


	public void setExecuted_amount(Double executed_amount) {
		this.executed_amount = executed_amount;
	}


	public String getShortName() {
		
		return (this.symbol+"    ").substring(0, 2);
	}

}
