package btc.model;

import org.json.JSONObject;

public class TickerV1 {

	double mid;
	double bid;
	double ask;
	double last_price;
	double low;
	double high;
	double volume;
	double timestamp;
	
	
	public TickerV1(JSONObject jsonO) throws Exception{
		
		this.mid= jsonO.getDouble("mid");
		this.bid = jsonO.getDouble("bid");
		this.ask =jsonO.getDouble("ask");
		this.last_price = jsonO.getDouble("last_price");
		this.low = jsonO.getDouble("low");
		this.high = jsonO.getDouble("high");
		this.volume = jsonO.getDouble("volume");
		this.timestamp = jsonO.getDouble("timestamp");
	}


	public double getMid() {
		return mid;
	}


	public double getBid() {
		return bid;
	}


	public double getAsk() {
		return ask;
	}


	public double getLast_price() {
		return last_price;
	}


	public double getLow() {
		return low;
	}


	public double getHigh() {
		return high;
	}


	public double getVolume() {
		return volume;
	}
	
}
