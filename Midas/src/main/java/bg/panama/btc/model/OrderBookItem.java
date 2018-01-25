package bg.panama.btc.model;

import java.text.DecimalFormat;

import org.json.JSONObject;

public class OrderBookItem {

	double amount ;
	double price;
	public OrderBookItem(JSONObject jso) throws Exception {
		amount = jso.getDouble("amount");
		price = jso.getDouble("price");
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	private static DecimalFormat df = new DecimalFormat("00.0000");
	@Override
	public String toString() {
		return "OrderBookItem [amount=" +df.format(amount)  + ", \tprice=" + price + "]";
	}

	
}
