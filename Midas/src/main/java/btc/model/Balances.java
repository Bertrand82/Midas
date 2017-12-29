package btc.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import btc.BitfinexClient.EnumService;
import btc.model.v2.ITicker;
import btc.model.v2.Ticker;
import btc.trading.first.Z_1_listCurrencies;

public class Balances {

	private List<Balance> lBalances = new ArrayList<>();
	private List<Balance> lBalancesExchange = new ArrayList<>();
	public Balances(JSONObject jo) throws Exception{
		System.out.println("Constructeur Balances");
		try {
			JSONArray array = jo.getJSONArray(EnumService.balances.key);
			System.out.println("Constructeur Balances length :"+array.length());
			for (int i = 0; i < array.length(); i++) {
				Object o = array.get(i);
				System.out.println(i+" BalanceXXX -----> o:"+ o+"   "+ array.get(i).getClass());
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
	public void process(Z_1_listCurrencies z) {
		double totalDollar =0;
		for(Balance balance : this.lBalancesExchange){
			double available = balance.getAmount();
			String currency = balance.getCurrency();
			double lastPrice = z.getLastPrice(currency);
			double percent = z.getDaylyChangePerCent(currency);
			double availableInDollar = lastPrice * available;
			System.err.println("Devise :"+currency+"\t|dayly change per cent : "+df2.format(percent*100)+"| available :"+df.format(available)+"\t| daily "+df.format(lastPrice) +"\t| in dollar "+df.format(availableInDollar));
			totalDollar+= availableInDollar;
		}
		System.err.println("Total available In dollar : " +df.format(totalDollar));
	}
}
