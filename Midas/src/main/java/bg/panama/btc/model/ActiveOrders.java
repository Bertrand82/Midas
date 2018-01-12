package bg.panama.btc.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bg.panama.btc.BitfinexClient.EnumService;

public class ActiveOrders {
    List<ActiveOrder> lOrders = new ArrayList<>();
    
	public ActiveOrders(JSONObject jo) throws Exception {
		try {
			JSONArray array = jo.getJSONArray(EnumService.orders.key);
			for (int i = 0; i < array.length(); i++) {
				Object o = array.get(i);
				ActiveOrder activeOrder = new ActiveOrder((JSONObject) array.get(i));
				this.lOrders.add(activeOrder);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public String toString() {
		
		return "orders "+lOrders;
	}

	public List<ActiveOrder> getlOrders() {
		return lOrders;
	}

}
