package bg.panama.btc.trading.first;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import bg.panama.btc.BitfinexClient.OrderType;
@Entity
public class Order implements Serializable {

	/**
	 * 
	 */
	public enum Side {
		buy,
		sell
	}
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;

	private String currency;
	private Side side;
	private double price;
	private String comment ="";
	private double amount;
	private Date date = new Date();
	public Order() {
		
	}

	public Order(String currency,  double ammont, Side side) {
		super();
		this.currency= currency;
		this.amount = ammont;
		this.side = side;
	}

	

	public double getAmmount() {
		return amount;
	}

	@Override
	public String toString() {
		String s ="";
		
		if(side != null){
			s += " "+side;
		}
		
		if(price != 0){
			s+=" price "+price;
			s += " AmountDesired "+getAmountDesired();
		}
		if (orderType != null){
			s += " orderType "+orderType;
		}
		return "Order From=" + currency+ ", Side=" + side + ", ammont=" + amount + ""+s;
	}

	public String getSymbol() {
		return (currency +"usd").toLowerCase();
	}

	

	
	public double getPrice() {
		return price;
	}

	/*
	 * 
	 * Either “buy” or “sell”.
	 */
	public Side getSide() {
		return this.side;
	}

	public void setSide(Side side) {
		this.side = side;
	}


	public boolean isSelling() {
		
		return side==Side.sell;
	}
	public boolean isBuying() {
		
		return side==Side.buy;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmountDesired() {
		if (isSelling()){
			return (getAmmount()) *0.999;
		}else if(isBuying()){
			return (getAmmount()/price) *0.9;
		}
		return 0;
	}

	
	//OrderType orderType = OrderType.limit;  
	OrderType orderType = OrderType.exchange_limit;// Working
	public OrderType getOrderType() {
		
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCurrency() {
		return currency;
	}

	

}
