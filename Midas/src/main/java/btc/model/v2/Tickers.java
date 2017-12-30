package btc.model.v2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import btc.BitfinexClient.EnumService;

public class Tickers {

	List<ITicker> lTickers = new ArrayList<ITicker>();
	List<ITicker> lTickersOrdered = new ArrayList<ITicker>();
	public Tickers(JSONObject jo) throws Exception{
		try {
			JSONArray array = jo.getJSONArray(EnumService.tickersV2.key);
			for (int i = 0; i < array.length(); i++) {
				Ticker ticker = new Ticker((JSONArray )array.get(i));
				this.lTickers.add(ticker);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.lTickersOrdered.addAll(this.lTickers);
		Collections.sort(lTickersOrdered, ITicker.comparatorDailyChangePercent);

	}
	

	static final NumberFormat formatter = new DecimalFormat("0.0000"); 
	
	@Override
	public String toString() {
		String s ="Best : "+lTickersOrdered.get(0).getShortName()+" |";
         s +="Worse : "+lTickersOrdered.get(lTickersOrdered.size()-1).getShortName()+" |";
		for(ITicker t : lTickers){
			s+= t.getShortName() +" : "+formatter.format(t.getDaylyChangePerCent())+"|";
		}
		return s ;
	}

	public List<ITicker> getlTickersOrdered() {
		Collections.sort(lTickersOrdered, ITicker.comparatorDailyChangePercent);
		return lTickersOrdered;
	}

	public List<ITicker> getlTickers() {
		return lTickers;
	}

	
}
