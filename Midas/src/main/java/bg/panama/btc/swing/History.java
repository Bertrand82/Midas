package bg.panama.btc.swing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bg.panama.btc.model.v2.ITicker;
import bg.panama.btc.trading.first.SessionCurrency;
import bg.panama.btc.util.PointBtc;

public class History implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SIZE_MAX = 120;

	List<SessionCurrency> listSessionCurrency = new ArrayList<>();

	public List<PointBtc> getListPointsVariation() {
		List<PointBtc> list = new ArrayList<>();
		for (SessionCurrency s : listSessionCurrency) {
			double chd = s.getHourlyChangePerCentByDay();
			if ((chd - SessionCurrency.D_default) < 0.000001) {
				chd = 0;
			} else if (chd > 10) {
				chd = 10.0;
			} else if (chd < -10) {
				chd = -10.0;
			}
			PointBtc p = new PointBtc(s.getDateLastUpdateAsLong(), chd);
			list.add(p);
		}

		return list;
	}

	public List<PointBtc> getListPointsPrixBrut() {
		List<PointBtc> list = new ArrayList<>();
		if (listSessionCurrency.isEmpty()) {
			return list;
		}
		//double lastPriceReference = listSessionCurrency.get(listSessionCurrency.size() - 1).getLastPrice();
		double lastPriceReference = listSessionCurrency.get(0).getLastPrice();
		for (SessionCurrency s : listSessionCurrency) {
			ITicker t = s.getTicker_Z_1();
			double lastPrice ;
			if(t != null){
				lastPrice = t.getLastPrice();
			}else {
				lastPrice = 0; 
			}
			double chd = 100 * (lastPrice - lastPriceReference) / lastPriceReference;
			PointBtc p = new PointBtc(s.getDateLastUpdateAsLong(), chd);
			list.add(p);
		}
		return list;
	}
	
	public List<PointBtc> getListPointsPrixFiltre() {
		List<PointBtc> list = new ArrayList<>();
		if (listSessionCurrency.isEmpty()) {
			return list;
		}
		//double lastPriceReference = listSessionCurrency.get(listSessionCurrency.size() - 1).getLastPrice();
		double lastPriceReference = listSessionCurrency.get(0).getLastPrice();
		for (SessionCurrency s : listSessionCurrency) {
			double lastPrice  = s.getLastPrice(); 
			double chd = 100 * (lastPrice - lastPriceReference) / lastPriceReference;
			PointBtc p = new PointBtc(s.getDateLastUpdateAsLong(), chd);
			list.add(p);
		}
		return list;
	}

	public void add(SessionCurrency clone) {
		this.listSessionCurrency.add(0, clone);
		if (this.listSessionCurrency.size() > SIZE_MAX) {
			this.listSessionCurrency.remove(SIZE_MAX);
		}
	}

}
