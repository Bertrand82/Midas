package btc.trading.first;

import java.io.Serializable;
import java.util.Date;

import btc.model.v2.ITicker;

public class SessionCurrency implements ITicker,Serializable{


	private static final long serialVersionUID = 1L;
	
	private final Date dateStart = new Date();
	Date date = new Date();
	double constanteTemps = 60 *60 * 1000;
	int nombreEchantillonMini=10;
	int nombreEchantillon=0;
	String name ;
	String shortName;
	double volumeDaily=0;
	double highDaily=0;
	double lowDaily=0;
	double lastPrice=0;
	double daylyChange=0;
	double daylyChangePerCent=0;
	private ITicker ticker_Z_1;
	private boolean isEligible= true;
	
	public SessionCurrency(ITicker ticker) {
		super();
		this.name = ticker.getName();
		this.shortName = ticker.getShortName();
		this.update(ticker);
	}

	public void update(ITicker ticker){
		this.ticker_Z_1 =ticker;
		this.volumeDaily = tranformZ(this.volumeDaily,ticker.getVolumeDaily());
		this.highDaily = tranformZ(this.highDaily,ticker.getHighDaily());
		this.lowDaily = tranformZ(this.lowDaily,ticker.getLowDaily());
		this.lastPrice = this.tranformZ(this.lastPrice, ticker.getLastPrice());
		this.daylyChange = this.tranformZ(daylyChange, ticker.getDaylyChange());
		this.daylyChangePerCent = this.tranformZ(daylyChangePerCent,ticker. getDaylyChangePerCent());
		nombreEchantillon++;
		this.date=new Date();
	}

	private double tranformZ(double v, double vNew) {
		double k = getK();
		return ( (k * v) +  vNew)/(k+1);
	}

	private double getK() {
		double k;
		long duree = System.currentTimeMillis() - this.date.getTime();
		long dureeFromStart = System.currentTimeMillis() - this.dateStart.getTime();
		if (nombreEchantillon == 0){
			k = 0;
		}else if(dureeFromStart < this.constanteTemps){					
			k=nombreEchantillon;
		}else {
			k = (this.constanteTemps)/duree;
		}
		return k;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getVolumeDaily() {
		return volumeDaily;
	}

	public void setVolumeDaily(double volumeDaily) {
		this.volumeDaily = volumeDaily;
	}

	public double getHighDaily() {
		return highDaily;
	}

	public void setHighDaily(double highDaily) {
		this.highDaily = highDaily;
	}

	public double getLowDaily() {
		return lowDaily;
	}

	public void setLowDaily(double lowDaily) {
		this.lowDaily = lowDaily;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double getDaylyChange() {
		return daylyChange;
	}

	public void setDaylyChange(double daylyChange) {
		this.daylyChange = daylyChange;
	}

	public double getDaylyChangePerCent() {
		return daylyChangePerCent;
	}

	public void setDaylyChangePerCent(double daylyChangePerCent) {
		this.daylyChangePerCent = daylyChangePerCent;
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return shortName;
	}

	public ITicker getTicker_Z_1() {
		return ticker_Z_1;
	}

	public boolean isEligible() {
		return isEligible;
	}

	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}

	public void updateWithArchive(SessionCurrency sArchive) {
		if(sArchive == null){
			return;
		}
		assert(name.equals(sArchive.name));
		assert(dateStart.after(sArchive.dateStart));
		isEligible = sArchive.isEligible;
	}
	
	
}
