package bg.panama.btc.model.v2;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bg.panama.btc.BitfinexClient.EnumService;


@Entity
public class Tickers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy="tickers")
	List<Ticker> lTickers = new ArrayList<Ticker>();
	

	@Id
	@GeneratedValue
	long id;
	
	
	Date date = new Date();;
	
	public Tickers() {
		super();
	}



	public Tickers(JSONObject jo) throws Exception{
		try {
			JSONArray array = jo.getJSONArray(EnumService.tickersV2.key);
			for (int i = 0; i < array.length(); i++) {
				Ticker ticker = new Ticker((JSONArray )array.get(i),this);
				this.lTickers.add(ticker);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	static final NumberFormat formatter = new DecimalFormat("0.0000"); 
	
	@Override
	public String toString() {
		List<Ticker> l =   getLTickersOrdered();
		String s ="Best : "+l.get(0).getShortName()+" |";
         s +="Worse : "+l.get(l.size()-1).getShortName()+" |";
		for(ITicker t : lTickers){
			s+= t.getShortName() +" : "+formatter.format(t.getDaylyChangePerCent())+"|";
		}
		return s ;
	}

	

	public List<Ticker> getlTickers() {
		return lTickers;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public void setlTickers(List<Ticker> lTickers) {
		this.lTickers = lTickers;
	}
	
	public List<Ticker>  getLTickersOrdered() {
		List<Ticker> lTickersOrdered = new ArrayList<>();
		lTickersOrdered.addAll(this.lTickers);
		Collections.sort(lTickersOrdered);
		return lTickersOrdered;
    }


	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public Ticker getTickerByName(String name) {
		for(Ticker t : this.lTickers){
			if (t.getName().equalsIgnoreCase(name)){
				return t;
			}
		}
		return null;
	}

	
}
