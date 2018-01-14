package bg.panama.btc.swing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import bg.panama.btc.model.v2.ITicker;
import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrenciesFactory;
import bg.panama.btc.trading.first.SessionCurrency;
import bg.util.PointDouble;

public class History implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SIZE_MAX = 120;
	
	
	

	
	private List<SessionCurrency> listSessionCurrency_ = null;
	private SessionCurrency sessionCurrency;
	private String name;

	public History( SessionCurrency sessionCurrency ) {		
		this.sessionCurrency=sessionCurrency;
		this.name = sessionCurrency.getName();
	}

	public List<PointDouble> getListPointsVariation() {
		List<PointDouble> list = new ArrayList<>();
		for (SessionCurrency s : getListSessionCurrency()) {
			double chd = s.getHourlyChangePerCentByDay();
			if ((chd - SessionCurrency.D_default) < 0.000001) {
				chd = 0;
			} else if (chd > 10) {
				chd = 10.0;
			} else if (chd < -10) {
				chd = -10.0;
			}
			PointDouble p = new PointDouble(s.getDateLastUpdateAsLong(), chd);
			list.add(p);
		}

		return list;
	}

	public List<PointDouble> getListPointsPrixBrut() {
		List<PointDouble> list = new ArrayList<>();
		if (getListSessionCurrency().isEmpty()) {
			return list;
		}
		//double lastPriceReference = listSessionCurrency.get(listSessionCurrency.size() - 1).getLastPrice();
		double lastPriceReference = getListSessionCurrency().get(0).getLastPrice();
		for (SessionCurrency s : getListSessionCurrency()) {
			Ticker t = s.getTicker_Z_1();
			
			double lastPrice ;
			if(t != null){
				lastPrice = t.getLastPrice();
				
			}else {
				lastPrice = 0; 
			}
			double delta = Math.abs(lastPrice - s.getLastPrice());
			//System.out.println("LastPrice Brut "+name+"  K :"+s.getK()+"  Ticker Last Price:\t"+lastPrice+"\t  Session Last Price\t "+s.getLastPrice()+" \tdelta : "+delta);
			double chd = 100 * (lastPrice - lastPriceReference) / lastPriceReference;
			PointDouble p = new PointDouble(s.getDateLastUpdateAsLong(), chd);
			list.add(p);
		}
		return list;
	}
	
	public List<PointDouble> getListPointsPrixFiltre() {
		List<PointDouble> list = new ArrayList<>();
		if (getListSessionCurrency().isEmpty()) {
			return list;
		}
		//double lastPriceReference = listSessionCurrency.get(listSessionCurrency.size() - 1).getLastPrice();
		double lastPriceReference = getListSessionCurrency().get(0).getLastPrice();
		for (SessionCurrency s : getListSessionCurrency()) {
			double lastPrice  = s.getLastPrice(); 
			double chd = 100 * (lastPrice - lastPriceReference) / lastPriceReference;
			PointDouble p = new PointDouble(s.getDateLastUpdateAsLong(), chd);
			list.add(p);
		}
		return list;
	}

	public void add____DEPRECATED(SessionCurrency clone) {
		this.getListSessionCurrency().add(0, clone);
		if (this.getListSessionCurrency().size() > SIZE_MAX) {
			this.getListSessionCurrency().remove(SIZE_MAX);
		}
	}

	

	public List<SessionCurrency> getListSessionCurrency() {
		if (this.listSessionCurrency_ == null){
			this.listSessionCurrency_  = SessionCurrenciesFactory.instance.getSessionsCurrency(SIZE_MAX,this.name);
			this.listSessionCurrency_.add(0, this.sessionCurrency);
			//System.out.println("getListSessionCurrency2 "+this.name+"  "+listSessionCurrency_);
		}
		return listSessionCurrency_;
	}

	

}
