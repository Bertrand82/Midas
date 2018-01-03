package btc.trading.first;

import java.io.Serializable;

import java.util.Date;

import btc.model.v2.ITicker;
import btc.model.v2.Ticker;

public class SessionCurrency implements ITicker,Serializable{


	private static final long serialVersionUID = 1L;
	
	private final Date dateStart = new Date();
	Date date = new Date();
	double constanteTemps = 60 *60 * 1000;
	int nombreEchantillonMini=10;
	int nombreEchantillon=0;
	String name ;	
	String shortName;
	double hourlyPrice =0;
	double volumeDaily=0;
	double highDaily=0;
	double lowDaily=0;
	double lastPrice=0;
	double daylyChange=0;
	double daylyChangePerCent=0;
	private ITicker ticker_Z_1;
	private boolean isEligible= true;
	private double hourlyChangePerCent;
	private long deltaTemps_ms_;
	private Date dateLastUpdate = new Date();
	int numero =0;
	public SessionCurrency(ITicker ticker) {
		super();
		this.name = ticker.getName();
		this.shortName = ticker.getShortName();
		this.update(ticker);
	}

	public void update(ITicker ticker){
		if (ticker_Z_1 != null){			
			double delta = ticker.getHourlyPrice()-ticker_Z_1.getHourlyPrice();
			//System.err.println("delta : "+delta+" getHourlyPrice "+ticker.getHourlyPrice()+"    ");
			if (ticker.getHourlyPrice() >0.00001){
				ticker.setHourlyChangePerCent(delta/this.hourlyPrice);
			}
			this.deltaTemps_ms_ = ( ticker.getDate().getTime()-ticker_Z_1.getDate().getTime());
			ticker.setDeltaTemps_ms(deltaTemps_ms_);
		}	
		this.ticker_Z_1 =ticker;
		ticker.setNumero(this.numero);
		this.numero++;
		this.hourlyPrice = tranformZ(this.hourlyPrice,ticker.getHourlyPrice());
		this.volumeDaily = tranformZ(this.volumeDaily,ticker.getVolumeDaily());
		this.highDaily = tranformZ(this.highDaily,ticker.getHighDaily());
		this.lowDaily = tranformZ(this.lowDaily,ticker.getLowDaily());
		this.lastPrice = this.tranformZ(this.lastPrice, ticker.getLastPrice());
		this.daylyChange = this.tranformZ(daylyChange, ticker.getDaylyChange());
		this.daylyChangePerCent = this.tranformZ(daylyChangePerCent,ticker. getDaylyChangePerCent());
			
		this.hourlyChangePerCent=this.tranformZ(hourlyChangePerCent,ticker.getHourlyChangePerCent());
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
	public double getHourlyPrice() {
		return hourlyPrice;
	}

	public double getHourlyChangePerCent() {
		return hourlyChangePerCent;
	}

	public void setHourlyChangePerCent(double hourlyChangePerCent) {
		this.hourlyChangePerCent = hourlyChangePerCent;
	}
	private static final double DAY_ms = 24 * 60*60*10000;
	
	
	@Override
	public long getDeltaTemps_ms() {
		
		return this.deltaTemps_ms_;
	}

	@Override
	public void setDeltaTemps_ms(long delta) {
		this.deltaTemps_ms_ =delta;
		
	}

	@Override
	public Date getDate() {
		
		return this.dateLastUpdate;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	public double getHourlyChangePerCentByDay() {
		double delta = getHourlyChangePerCent();
		return getHourlyChangePerCentByDay(delta);
	}
	

	public double getHourlyChangePerCentByDayInstant() {
		double delta = this.ticker_Z_1.getHourlyChangePerCent();
		return getHourlyChangePerCentByDay(delta);
	}
	
	public static final double D_default= -100000000;
	private double getHourlyChangePerCentByDay(double delta) {
		if (this.ticker_Z_1.getNumero() ==0){
			return D_default;
		}
		long dt_ms = this.ticker_Z_1.getDeltaTemps_ms();
		
		if( dt_ms  ==0){
			return -100;// Ne devrait pas arriver
		}
	    double delta_t =( 1.0*dt_ms)/DAY_ms;
	   
		double chd = delta/delta_t;
		return chd;
	    
	}

	public boolean isInitializing() {
		
		return numero <= 1;
	}



	
}
