package btc.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import btc.BitfinexClient.EnumService;
import btc.model.v2.ITicker;
import btc.model.v2.Ticker;
import btc.trading.first.Order;
import btc.trading.first.Z_1_listCurrencies;

public class Balances {

	private static final Logger loggerTradeBalance= LogManager.getLogger("tradeBalance");
	private List<Balance> lBalances = new ArrayList<>();
	private List<Balance> lBalancesExchange = new ArrayList<>();
	public Balances(JSONObject jo) throws Exception{
		try {
			JSONArray array = jo.getJSONArray(EnumService.balances.key);
			for (int i = 0; i < array.length(); i++) {
				Object o = array.get(i);
				Balance balance = new Balance((JSONObject )array.get(i));
				this.lBalances.add(balance);
				if (balance.isTypeEchange()){
					this.lBalancesExchange.add(balance);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	DecimalFormat df = new DecimalFormat("0000000.00");
	DecimalFormat df2 = new DecimalFormat("00.00");
	public List<Order> process(Z_1_listCurrencies z) {
		List<Order> orders = new ArrayList<>();
		double totalDollar =0;
		ITicker tickerBest = z.getListOrder_byDailyChangePerCent().get(0);
		for(Balance balance : this.lBalancesExchange){
			double available = balance.getAmount();
			String currency = balance.getCurrency();
			double lastPrice = z.getLastPrice(currency);
			double percent = z.getDaylyChangePerCent(currency);
			double deltaPercent = tickerBest.getDaylyChangePerCent() - percent;
			double availableInDollar = lastPrice * available;
			loggerTradeBalance.info("Devise :"+currency+"\t|dayly change per cent : "+df2.format(percent*100)+"| available :"+df.format(available)+"\t| daily "+df.format(lastPrice) +"\t| in dollar "+df.format(availableInDollar));
			totalDollar+= availableInDollar;
			
			if(tickerBest.getShortName().equals(currency)){
				//PAs d'ordres
			}else if(deltaPercent < 0.01){
				//Introduction de stabilité (eviter les yoyo)
		    }else {
		    	if(availableInDollar < 30){
		    		// minimum order size between 10-25 USD
		    	}else {
		    		Order order = new Order(currency, tickerBest.getShortName(),available);
		    		orders.add(order);
		    	}
				
			}
		}
		loggerTradeBalance.info("Total available In dollar : " +df.format(totalDollar));
		loggerTradeBalance.info("List Orders "+orders);
		return orders;
	}
	@Override
	public String toString() {
		return "Balances [lBalances=" + lBalances + "]";
	}
	
	
}
