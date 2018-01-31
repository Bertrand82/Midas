package  bg.panama.btc.trading.first;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import bg.panama.btc.BitfinexClient.OrderType;
import bg.panama.btc.model.operation.Operation;
// Il faut donner un nom à la table, parceque Order ca plante!!!!! 
@Entity
@Table(name="ORDER_BTFX2")
public class Order implements Serializable {

	/**
	 * 
	 */
	public static enum Side {
		buy,
		sell
	}
	public enum TypeChoicePrice {
		panic, 
		fromTickers, 
		fromBookOrder,
		manual
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;

	private String currency="";	
	private Side  side;
	private TypeChoicePrice typeChoicePrice;
	private double price =0d;
	private String comment ="No Comment";
	private double amount=0d;
	private Date date = new Date();
	//private Operation operation;
	
	

	public Order() {
	}

	public Order(String currency,  double ammont, Side side, TypeChoicePrice typeChoicePrice) {
		super();
		this.currency= currency;
		this.amount = ammont;
		this.side = side;
		this.typeChoicePrice = typeChoicePrice;
		System.out.println("order "+this);
	}

	

	public double getAmmount() {
		return amount;
	}

	@Override
	public String toString() {
		String s ="";
		
		
			s += " "+side;
	
		
		if(price != 0){
			s+=" price "+price;
			s += " AmountDesired "+getAmountDesired();
		}
		if (orderType != null){
			s += " orderType "+orderType;
		}
		s +="   ChoicePrice :"+ this.typeChoicePrice;
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
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}
	

	public boolean isSelling() {
		
		return getSide()==Side.sell;
	}
	public boolean isBuying() {
		
		return getSide()==Side.buy;
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

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public TypeChoicePrice getTypeChoicePrice() {
		return typeChoicePrice;
	}

	public void setTypeChoicePrice(TypeChoicePrice typeChoicePrice) {
		this.typeChoicePrice = typeChoicePrice;
	}

	public String getMessageConfirmation() {
		return this.side+" "+this.amount+"  "+this.currency;
	}

	

}
