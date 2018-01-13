package bg.panama.btc.model.v2;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
@Entity
public class Ticker implements Comparable<Ticker>, ITicker, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id ;
	String name ;
	double hourlyPrice;
	double volumeDaily;
	double highDaily;
	double lowDaily;
	double lastPrice;
	double daylyChange;
	double daylyChangePerCent;
	double hourlyChangePerCent;
	long deltaTemps_=10000000000000000l;
	Date date = new Date();
	int numero ;
	@ManyToOne
	@JoinColumn(name="tickers_id", nullable=false)
	Tickers tickers;
	/*
	 
0	["tBTCUSD",
1	13133,		Flash Return Rate - average of all fixed rate funding over the last hour
2	34.86896499,
3	13134,58.
4	4337667,
5	-421,		daylyChange
6	-0.0311,	daylyChangePerCent
7	13134,		lastPrice
8	35459.		volumeDaily
9	46177273,	
10	13898,		highDaily
11	12777]		lowDaily
 
	 
	 
	 */
	
	public Ticker() {
		
	}
	
	public Ticker(JSONArray jsonArray,Tickers tickers) throws Exception{
		
		this.name= jsonArray.getString(0);
		this.hourlyPrice = jsonArray.getDouble(1);
		this.daylyChange = jsonArray.getDouble(5);
		this.daylyChangePerCent =jsonArray.getDouble(6);
		this.lastPrice = jsonArray.getDouble(7);
		this.volumeDaily = jsonArray.getDouble(8);
		this.highDaily = jsonArray.getDouble(9);
		this.lowDaily = jsonArray.getDouble(10);
		this.tickers =tickers;
	}



	@Override
	public String toString() {
		return "Ticker [name=" + name +" hourlyPrice="+hourlyPrice+ ", volumeDaily=" + volumeDaily + ", highDaily=" + highDaily + ", lowDaily="
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



	public double getHourlyPrice() {
		return hourlyPrice;
	}



	public void setHourlyPrice(double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
	}



	@Override
	public double getHourlyChangePerCent() {
		return hourlyChangePerCent;
	}



	public void setHourlyChangePerCent(double hourlyChangePerCent) {
		this.hourlyChangePerCent = hourlyChangePerCent;
	}



	@Override
	public long getDeltaTemps_ms() {
		return deltaTemps_;
	}



	@Override
	public void setDeltaTemps_ms(long delta) {
		this.deltaTemps_ =delta;
		
	}



	@Override
	public Date getDate() {
		return date;
	}



	public int getNumero() {
		return numero;
	}



	public void setNumero(int numero) {
		this.numero = numero;
	}



	public long getDeltaTemps_() {
		return deltaTemps_;
	}



	public void setDeltaTemps_(long deltaTemps_) {
		this.deltaTemps_ = deltaTemps_;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public Tickers getTickers() {
		return tickers;
	}



	public void setTickers(Tickers tickers) {
		this.tickers = tickers;
	}



	

}
