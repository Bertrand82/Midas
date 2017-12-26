package btc.model.v2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import btc.BitfinexClient.EnumService;

public class Tickers {

	List<Ticker> lTickers = new ArrayList<Ticker>();
	public Tickers(JSONObject jo) throws Exception{
		System.out.println("Constructeur Tickers");
		try {
			JSONArray array = jo.getJSONArray(EnumService.tickersV2.key);
			for (int i = 0; i < array.length(); i++) {
				System.out.println(i+" Tickers -----> " + array.get(i).getClass());
				Ticker ticker = new Ticker((JSONArray )array.get(i));
				this.lTickers.add(ticker);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
