package bg.panama.btc.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



public class OrderBook {

	static String jsonStr = "{\"asks\":[{\"amount\":\"5.20855112\",\"price\":\"11876\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.02\",\"price\":\"11877\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.91\",\"price\":\"11879\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"4.8672655\",\"price\":\"11880\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1\",\"price\":\"11883\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"2.5\",\"price\":\"11884\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"3.64012487\",\"price\":\"11890\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"5.9\",\"price\":\"11892\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"6.7\",\"price\":\"11893\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.02925\",\"price\":\"11894\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.15000001\",\"price\":\"11899\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"94.04772455\",\"price\":\"11900\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.10753753\",\"price\":\"11901\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.7\",\"price\":\"11902\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.01\",\"price\":\"11905\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"6.0828\",\"price\":\"11906\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.02\",\"price\":\"11907\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1.4\",\"price\":\"11908\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"10.64205486\",\"price\":\"11911\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1.55522553\",\"price\":\"11912\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"6.41\",\"price\":\"11913\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.2\",\"price\":\"11914\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1.0429455\",\"price\":\"11915\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.02640927\",\"price\":\"11916\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.02274136\",\"price\":\"11917\",\"timestamp\":\"1516608734.0\"}],\"bids\":[{\"amount\":\"0.00446605\",\"price\":\"11875\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.91110208\",\"price\":\"11867\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.7\",\"price\":\"11866\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.4483\",\"price\":\"11861\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1.02\",\"price\":\"11860\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1\",\"price\":\"11859\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.88240825\",\"price\":\"11858\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.00462902\",\"price\":\"11857\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"6.6\",\"price\":\"11854\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"10.04944119\",\"price\":\"11853\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1.5\",\"price\":\"11851\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"63.21784999\",\"price\":\"11850\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"6.7\",\"price\":\"11849\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1\",\"price\":\"11848\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.02\",\"price\":\"11845\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"7.05\",\"price\":\"11842\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.89999999\",\"price\":\"11841\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.82\",\"price\":\"11840\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"10.06440839\",\"price\":\"11838\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"6.06995\",\"price\":\"11837\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.831\",\"price\":\"11836\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"1.2\",\"price\":\"11835\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"2.644\",\"price\":\"11834\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"0.5\",\"price\":\"11832\",\"timestamp\":\"1516608734.0\"},{\"amount\":\"6\",\"price\":\"11831\",\"timestamp\":\"1516608734.0\"}]}";
	OrderBookFactory factory;
	List<OrderBookItem> listAsks = new ArrayList<>();
	List<OrderBookItem> listBids = new ArrayList<>();
	double ammountTotalAsk = 0;
	double ammountTotalBids = 0;
	int ammountTotalAskPercent = 0;
	int ammountToyalBidPercent = 0;
	OrderBookItem orderBookItemMax;
	OrderBookItem orderBookItemMin;
	OrderBookItem orderBookItemAmmountMax;
	double maxPrice;
	double minPrice;
	double amountMAx;

	public OrderBook() throws Exception {
		this(new JSONObject(jsonStr));
	}

	public OrderBook(JSONObject jo) {
		System.out.println("Constructeur OrderBook");
		try {
			JSONArray arrayAsk = jo.getJSONArray("asks");
			for (int i = 0; i < arrayAsk.length(); i++) {
				JSONObject jso = (JSONObject) arrayAsk.get(i);
				OrderBookItem oi = new OrderBookItem(jso);
				listAsks.add(oi);
			}
			JSONArray arrayBids = jo.getJSONArray("bids");
			for (int i = 0; i < arrayBids.length(); i++) {
				JSONObject jso = (JSONObject) arrayBids.get(i);
				OrderBookItem oi = new OrderBookItem(jso);
				listBids.add(oi);

			}
			this.process();
		} catch (Exception e) {
			System.err.println("Error B552 JSONException " + e.getMessage() + " | JsdonObject :" + jo);
		}
	}

	
	public List<OrderBookItem> getListAsks() {
		return listAsks;
	}

	public void setListAsks(List<OrderBookItem> listAsks) {
		this.listAsks = listAsks;
	}

	public List<OrderBookItem> getListBids() {
		return listBids;
	}

	public void setListBids(List<OrderBookItem> listBids) {
		this.listBids = listBids;
	}

	private void process() {
		List<OrderBookItem> listAll = new ArrayList<>();
		listAll.addAll(this.listAsks);
		listAll.addAll(this.listBids);
		maxPrice = listAll.get(0).getPrice();
		minPrice = listAll.get(0).getPrice();
		amountMAx = listAll.get(0).getAmount();
		for (OrderBookItem obi : listAll) {
			if (obi.getPrice() > maxPrice){
				maxPrice = obi.getPrice();
			    orderBookItemMax =obi;
			}
			if (obi.getPrice() < minPrice){
				minPrice = obi.getPrice();
			    orderBookItemMin = obi;
			}
			if (obi.getAmount() > amountMAx){
				amountMAx = obi.getAmount();
				orderBookItemAmmountMax =obi;
			}
		}
		
		ammountTotalAsk = 0;
		ammountTotalBids = 0;
		for (OrderBookItem obi2 : this.getListAsks()) {
			ammountTotalAsk += obi2.getAmount();
		}
		for (OrderBookItem obi2 : this.getListBids()) {
			ammountTotalBids += obi2.getAmount();
		}
		ammountTotalAskPercent = (int) (ammountTotalAsk * 100 / (ammountTotalAsk + ammountTotalBids));
		ammountToyalBidPercent = (int) (ammountTotalBids * 100 / (ammountTotalAsk + ammountTotalBids));
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getAmmountTotalAsk() {
		return ammountTotalAsk;
	}

	public double getAmmountTotalBids() {
		return ammountTotalBids;
	}

	public int getAmmountTotalAskPercent() {
		return ammountTotalAskPercent;
	}

	public int getAmmountToyalBidPercent() {
		return ammountToyalBidPercent;
	}

	public double getAmountMAx() {
		return amountMAx;
	}

	public OrderBookItem getOrderBookItemMax() {
		return orderBookItemMax;
	}

	public OrderBookItem getOrderBookItemMin() {
		return orderBookItemMin;
	}

	public OrderBookItem getOrderBookItemAmmountMax() {
		return orderBookItemAmmountMax;
	}

	@Override
	public String toString() {
		return "OrderBook [ammountTotalAsk=" + ammountTotalAsk + ", ammountTotalBids=" + ammountTotalBids
				+ ", ammountTotalAskPercent=" + ammountTotalAskPercent + ", ammountToyalBidPercent="
				+ ammountToyalBidPercent + ", maxPrice=" + maxPrice + ", minPrice=" + minPrice + ", amountMAx="
				+ amountMAx + "]";
	}

	public double getPrice(boolean achat) {
		double price;
		if (achat){
			double perCentOfAmount =0.2;
			double priceAchat  = this.getHigerFromList(getListBids(),perCentOfAmount);
			price = priceAchat;
		}else {
			double perCentOfAmount =0.05;
			double priceVente  = this.getLowerFromList(getListAsks(),perCentOfAmount);
			price = priceVente;
		}
		return price;
	}

	public static Comparator<OrderBookItem> comparatorAscendant = new Comparator<OrderBookItem>() {

		@Override
		public int compare(OrderBookItem o1, OrderBookItem o2) {
			return  Double.compare(o1.price, o2.price);
			
		}
	};
	public static Comparator<OrderBookItem> comparatorDescendant = new Comparator<OrderBookItem>() {

		@Override
		public int compare(OrderBookItem o1, OrderBookItem o2) {
			return  -Double.compare(o1.price, o2.price);
			
		}
	};
	public double getLowerFromList(List<OrderBookItem> listAsks2, double percentageAmount) {
		List<OrderBookItem> l2  = new ArrayList<>();
		l2.addAll(listAsks2);
		Collections.sort(l2,comparatorAscendant );
		double total = 0;
		int i=0;
		for (OrderBookItem obi : l2){
			if (total >= percentageAmount * this.ammountTotalAsk){
				return obi.price *0.9999;
			}
			total += obi.getAmount();
			System.out.println(i+++" Ask obi :: "+obi);
		}
		return l2.get(0).getPrice()*0.9999;
	}

	public double getHigerFromList(List<OrderBookItem> listBids2, double percentageAmount) {
		List<OrderBookItem> l2  = new ArrayList<>();
		l2.addAll(listBids2);
		Collections.sort(l2,comparatorDescendant );
		double total = 0;
		int i=0;
		for (OrderBookItem obi : l2){
			if (total >= percentageAmount * this.ammountTotalAsk){
				return obi.price *0.9999;
			}
			total += obi.getAmount();
			System.out.println(i+++" Bids obi :: "+obi);
		}
		return l2.get(0).getPrice()*0.9999;
	}
	
	

}
