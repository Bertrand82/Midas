package btc.trading.first;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;

import btc.BitfinexClient.OrderType;

public class Order implements Serializable {

	/**
	 * 
	 */
	public static final String side_buy ="buy";
	public static final String side_sell ="sell";
	private static final long serialVersionUID = 1L;
	private String currencyFrom;
	private String currencyTo;
	private String side;
	private double price;
	private String symbolWithDirection;
		
	private double amount;
	private double amountToConvert;

	public Order(String currencyFrom, String currencyTo, double ammont) {
		super();
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.amount = ammont;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
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
		if(symbolWithDirection != null){
			s+=" "+symbolWithDirection;
		}
		if(price != 0){
			s+=" price "+price;
			s += " AmountDesired "+getAmountDesired();
		}
		if (orderType != null){
			s += " orderType "+orderType;
		}
		return "Order From=" + currencyFrom + ", To=" + currencyTo + ", ammont=" + amount + ""+s;
	}

	public String getSymbol() {
		return (currencyFrom + currencyTo).toLowerCase();
	}

	public String getSymbolInvers() {
		return (currencyTo + currencyFrom).toLowerCase();
	}

	
	public String getSymbolWithDirection() {
		return symbolWithDirection;
	}

	public void setSymbolWithDirection(String symbolWithDirection) {
		this.symbolWithDirection = symbolWithDirection;
	}


	
	public double getPrice() {
		return price;
	}

	/*
	 * 
	 * Either “buy” or “sell”.
	 */
	public String getSide() {
		return this.side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public double getAmmountToConvert() {
		return amountToConvert;
	}
	
	public void setAmmountToConvert(double ammountToConvert) {
		this.amountToConvert = ammountToConvert;
	}

	public boolean isSelling() {
		
		return side_sell.equals(this.side);
	}
	public boolean isBuying() {
		
		return side_buy.equals(this.side);
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

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}
	OrderType orderType = OrderType.limit;
	public OrderType getOrderType() {
		
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	

}
