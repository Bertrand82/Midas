package bg.panama.btc.trading.first;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.json.JSONObject;

/*
sendOrderPrivate retour :
{
"id":7586861883,
"cid":71815190572,
"cid_date":"2018-01-26",
"gid":null,"symbol":"rrtusd",
"exchange":"bitfinex",
"price":"0.11438",
"avg_execution_price":"0.0",
"side":"buy",
"type":"exchange limit",
"timestamp":"1516996615.291039236",
"is_live":true,
"is_cancelled":false,
"is_hidden":false,"oco_order":null,"was_forced":false,
"original_amount":"1748.53817837",
"remaining_amount":"1748.53817837",
"executed_amount":"0.0",
"src":"api",
"order_id":7586861883}


*/
@Entity
public class OrderRetour {

	
	@Id	
	long id;
	long cid;
	String cid_date;
	String symbol ;
	String exchange;
	double price;
	double avg_execution_price;
	String side;
	String type;
	long timestamp;
	boolean is_live;
	boolean is_cancelled;
	boolean is_hidden;
	double original_amount;
	double remaining_amount;
	double executed_amount;
	String src;
	long order_id;
	
	public OrderRetour(){
		
	}
	public OrderRetour(JSONObject jsonO) throws Exception{
		super();
		this.id= jsonO.getLong("id");
		this.cid = jsonO.getLong("cid");
		this.cid_date = jsonO.getString("cid_date");
		this.symbol = jsonO.getString("symbol");
		this.exchange = jsonO.getString("exchange");
		this.price = jsonO.getDouble("price");
		this.avg_execution_price = jsonO.getDouble("avg_execution_price");
		this.side =jsonO.getString("side");
		this.type = jsonO.getString("type");
		this.timestamp = jsonO.getLong("timestamp");
		this.is_live = jsonO.getBoolean("is_live");
		this.is_cancelled = jsonO.getBoolean("is_cancelled");
		this.is_hidden = jsonO.getBoolean("is_hidden");
		this.original_amount = jsonO.getDouble("original_amount");
		this.remaining_amount = jsonO.getDouble("remaining_amount");
		this.executed_amount = jsonO.getDouble("executed_amount");
		this.src = jsonO.getString("src");
		this.order_id = jsonO.getLong("order_id");
	}
	@Override
	public String toString() {
		return "OrderRetour [id=" + id + ", cid=" + cid + ", cid_date=" + cid_date + ", symbol=" + symbol
				+ ", exchange=" + exchange + ", price=" + price + ", avg_execution_price=" + avg_execution_price
				+ ", side=" + side + ", type=" + type + ", timestamp=" + timestamp + ", is_live=" + is_live
				+ ", is_cancelled=" + is_cancelled + ", is_hidden=" + is_hidden + ", original_amount=" + original_amount
				+ ", remaining_amount=" + remaining_amount + ", executed_amount=" + executed_amount + ", src=" + src
				+ ", order_id=" + order_id + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public String getCid_date() {
		return cid_date;
	}
	public void setCid_date(String cid_date) {
		this.cid_date = cid_date;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getAvg_execution_price() {
		return avg_execution_price;
	}
	public void setAvg_execution_price(double avg_execution_price) {
		this.avg_execution_price = avg_execution_price;
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
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isIs_live() {
		return is_live;
	}
	public void setIs_live(boolean is_live) {
		this.is_live = is_live;
	}
	public boolean isIs_cancelled() {
		return is_cancelled;
	}
	public void setIs_cancelled(boolean is_cancelled) {
		this.is_cancelled = is_cancelled;
	}
	public boolean isIs_hidden() {
		return is_hidden;
	}
	public void setIs_hidden(boolean is_hidden) {
		this.is_hidden = is_hidden;
	}
	public double getOriginal_amount() {
		return original_amount;
	}
	public void setOriginal_amount(double original_amount) {
		this.original_amount = original_amount;
	}
	public double getRemaining_amount() {
		return remaining_amount;
	}
	public void setRemaining_amount(double remaining_amount) {
		this.remaining_amount = remaining_amount;
	}
	public double getExecuted_amount() {
		return executed_amount;
	}
	public void setExecuted_amount(double executed_amount) {
		this.executed_amount = executed_amount;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}


}
