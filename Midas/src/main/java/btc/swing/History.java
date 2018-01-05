package btc.swing;

import java.util.ArrayList;
import java.util.List;

import btc.trading.first.SessionCurrency;
import btc.util.PointBtc;

public class History {

	
	private static final int SIZE_MAX = 60;
	
	List<SessionCurrency>  listSessionCurrency = new ArrayList<>();
	
	
	public List<PointBtc> getListPoints() {
		List<PointBtc> list = new ArrayList<>();
		for(SessionCurrency s :listSessionCurrency){
		    double chd = s.getHourlyChangePerCentByDay();
		    if (chd > 1) chd =1.0;
		    if (chd <1) chd = -1;
			PointBtc p = new PointBtc(s.getDateLastUpdateAsLong(), chd);
			list.add(p);
		}
		
		return list;
	}
	
	
	public void add(SessionCurrency clone) {
		this.listSessionCurrency.add(0, clone);
		if (this.listSessionCurrency.size() > SIZE_MAX){
			this.listSessionCurrency.remove(SIZE_MAX);
		}
		
	}

}
