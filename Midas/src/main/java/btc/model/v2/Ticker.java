package btc.model.v2;

import org.json.JSONArray;
/*
 * 
1 FRR	float	Flash Return Rate - average of all fixed rate funding over the last hour
2 BID	float	Price of last highest bid
3 BID_PERIOD	int	Bid period covered in days
4 BID_SIZE	float	Size of the last highest bid
5 ASK	float	Price of last lowest ask
6 ASK_PERIOD	int	Ask period covered in days
7 ASK_SIZE	float	Size of the last lowest ask
8 DAILY_CHANGE	float	Amount that the last price has changed since yesterday
9 DAILY_CHANGE_PERC	float	Amount that the price has changed expressed in percentage terms
10 LAST_PRICE	float	Price of the last trade
11 VOLUME	float	Daily volume
12 HIGH	float	Daily high
13 LOW	float	Daily low

 */
public class Ticker implements Comparable<Ticker>, ITicker{

	String name ;
	double volumeDaily;
	double highDaily;
	double lowDaily;
	double lastPrice;
	double daylyChange;
	double daylyChangePerCent;
	
	
	
	public Ticker(JSONArray jsonArray) throws Exception{
		System.out.println("jsonArray.length() "+jsonArray.length());
		
		System.out.println("jsonArray.length:: "+jsonArray.length());
		for(int i=0 ; i<jsonArray.length();i++){
			System.out.print("  :::["+i+"] "+jsonArray.get(i));
		}
		
		this.name= jsonArray.getString(0);
		this.daylyChange = jsonArray.getDouble(5);
		this.daylyChangePerCent =jsonArray.getDouble(6);
		this.lastPrice = jsonArray.getDouble(7);
		this.volumeDaily = jsonArray.getDouble(8);
		this.highDaily = jsonArray.getDouble(9);
		this.lowDaily = jsonArray.getDouble(10);

	}



	@Override
	public String toString() {
		return "Ticker [name=" + name + ", volumeDaily=" + volumeDaily + ", highDaily=" + highDaily + ", lowDaily="
				+ lowDaily + ", lastPrice=" + lastPrice + ", daylyChange=" + daylyChange + ", daylyChangePerCent="
				+ daylyChangePerCent + "]";
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getShortName()
	 */
	@Override
	public String getShortName() {
		if (name.length() > 4){
			return name.substring(1, 4);
		}else {
			return name;
		}
	}



	public int compareTo(Ticker o) {
		Double d = o.daylyChangePerCent;
		return d.compareTo(this.daylyChangePerCent);
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getName()
	 */
	@Override
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getVolumeDaily()
	 */
	@Override
	public double getVolumeDaily() {
		return volumeDaily;
	}



	public void setVolumeDaily(double volumeDaily) {
		this.volumeDaily = volumeDaily;
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getHighDaily()
	 */
	@Override
	public double getHighDaily() {
		return highDaily;
	}



	public void setHighDaily(double highDaily) {
		this.highDaily = highDaily;
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getLowDaily()
	 */
	@Override
	public double getLowDaily() {
		return lowDaily;
	}



	public void setLowDaily(double lowDaily) {
		this.lowDaily = lowDaily;
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getLastPrice()
	 */
	@Override
	public double getLastPrice() {
		return lastPrice;
	}



	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getDaylyChange()
	 */
	@Override
	public double getDaylyChange() {
		return daylyChange;
	}



	public void setDaylyChange(double daylyChange) {
		this.daylyChange = daylyChange;
	}



	/* (non-Javadoc)
	 * @see btc.model.v2.ITicker#getDaylyChangePerCent()
	 */
	@Override
	public double getDaylyChangePerCent() {
		return daylyChangePerCent;
	}



	public void setDaylyChangePerCent(double daylyChangePerCent) {
		this.daylyChangePerCent = daylyChangePerCent;
	}



	

}
