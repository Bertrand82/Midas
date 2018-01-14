package bg.panama.btc.trading.first;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import bg.panama.btc.model.v2.ITicker;
import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.swing.History;


@Entity
public class SessionCurrency implements Serializable,Cloneable{


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	private  Date dateStart = new Date();
	private Date date = new Date();
	private double constanteTemps = 10 *60 * 1000;
	private int nombreEchantillon=0;
	private String name ;	
	private String shortName;
	private double hourlyPrice =0;
	private double volumeDaily=0;
	private double highDaily=0;
	private double lowDaily=0;
	double lastPrice=0;
	
	@OneToOne ()
	private Ticker ticker_Z_1;
	private boolean isEligible= true;
	private double hourlyChangePerCent;
	private long deltaTemps_ms_;
	private long dateLastUpdate = System.currentTimeMillis();
	private int numero =0;
	@ManyToOne
	@JoinColumn(name="sessionCurrencies_id", nullable=false)
	private SessionCurrencies sessionCurrencies;
	
	@Transient
	private History history;
	
	public SessionCurrency(Ticker ticker, SessionCurrencies sessionCurrencies) {
		super();
		this.sessionCurrencies = sessionCurrencies;
		this.name = ticker.getName();
		this.shortName = ticker.getShortName();
		this.update(ticker);
		history = new History(this);
	}

	public  SessionCurrency() {
	}

	public void update(Ticker ticker){
		this.hourlyPrice = tranformZ(this.hourlyPrice,ticker.getHourlyPrice());
		this.dateLastUpdate = System.currentTimeMillis();
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
		
		
		this.volumeDaily = tranformZ(this.volumeDaily,ticker.getVolumeDaily());
		this.highDaily = tranformZ(this.highDaily,ticker.getHighDaily());
		this.lowDaily = tranformZ(this.lowDaily,ticker.getLowDaily());
		this.lastPrice = this.tranformZ(this.lastPrice, ticker.getLastPrice());
		this.hourlyChangePerCent=this.tranformZ(hourlyChangePerCent,ticker.getHourlyChangePerCent());
		System.err.println("getK3AAAA :  "+getK()+"   numero "+numero+"  "+name);
		nombreEchantillon++;
		this.date=new Date();
			//this.history.add((SessionCurrency)this.clone());
		this.numero++;
	}

	private double tranformZ(double v, double vNew) {
		double k = getK();
		
		return ( (k * v) +  vNew)/(k+1);
	}

	public double getK() {
		double k;
		long duree = System.currentTimeMillis() - this.date.getTime();
		long dureeFromStart = System.currentTimeMillis() - this.dateStart.getTime();
		if (nombreEchantillon == 0){
			k = 0;
		}else if(dureeFromStart < this.constanteTemps){					
			k = nombreEchantillon;
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

	
	
	public String getShortName() {
		// TODO Auto-generated method stub
		return shortName;
	}

	public Ticker getTicker_Z_1() {
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
	
	

	public long getDeltaTemps_ms() {
		
		return this.deltaTemps_ms_;
	}

	
	public void setDeltaTemps_ms(long delta) {
		this.deltaTemps_ms_ =delta;
		
	}

	
	public Date getDate() {
		
		return new Date(this.dateLastUpdate);
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	public double getHourlyChangePerCentByDay() {
		double delta = getHourlyChangePerCent();
		double delta_day = getHourlyChangePerCentByDay(delta,this.getDeltaTemps_ms());
		return delta_day;
	}
	

	public double getHourlyChangePerCentByDayInstant() {
		double delta = this.ticker_Z_1.getHourlyChangePerCent();
		return getHourlyChangePerCentByDay(delta);
	}
	
	public static final double D_default= -100000000;

	public static final SessionCurrency SessionCurrencyUSD = new SessionCurrency();
	static {
		SessionCurrencyUSD.lastPrice=1.0;
	}
	private double getHourlyChangePerCentByDay(double delta) {
		if (this.ticker_Z_1 == null){
			return D_default;
		}
		if (this.ticker_Z_1.getNumero() ==0){
			return D_default;
		}
		long dt_ms = this.ticker_Z_1.getDeltaTemps_ms();
		return getHourlyChangePerCentByDay(delta,dt_ms);
	}
	
	
	private double getHourlyChangePerCentByDay(double delta,long dt_ms){
		if( dt_ms  ==0){
			return D_default;// Ne devrait pas arriver
		}
	    double delta_t =( 1.0*dt_ms)/DAY_ms;
	   
		double chd = delta/delta_t;
		//System.err.println("getHourlyChangePerCentByDay  "+chd);
		return chd;
	    
	}

	public boolean isInitializing() {
		
		return numero <= 1;
	}
	
	
	public History getHistory() {		
		return history;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		
		SessionCurrency s = new SessionCurrency();
		s.nombreEchantillon = nombreEchantillon;
		s.date = date;
		s.dateLastUpdate= dateLastUpdate;
		s.dateStart =dateStart;
		s.deltaTemps_ms_= deltaTemps_ms_;
		s.highDaily = s.highDaily;
		s.hourlyChangePerCent=hourlyChangePerCent;
		s.hourlyPrice= hourlyPrice;
		s.lastPrice=lastPrice;
		s.lowDaily=lowDaily;
		s.numero=numero;
		s.shortName= shortName;
		s.volumeDaily=volumeDaily;
		s.ticker_Z_1 = ticker_Z_1;
		s.name = name;
		s.history = new History(this);
		return s;
	}

	public Date getDateLastUpdate_() {
		return new Date(dateLastUpdate);
	}

	public double getDateLastUpdateAsLong() {
		return this.dateLastUpdate;
	}

	public SessionCurrencies getSessionCurrencies() {
		return sessionCurrencies;
	}

	public void setSessionCurrencies(SessionCurrencies sessionCurrencies) {
		this.sessionCurrencies = sessionCurrencies;
	}

	@Override
	public String toString() {
		return "SessionCurrency [id=" + id + ", date=" + date + ", shortName=" + shortName + ", hourlyPrice="
				+ hourlyPrice + ", lastPrice=" + lastPrice + "]";
	}

	


	
}