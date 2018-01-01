package btc.model.v2;

import java.io.Serializable;
import java.util.Comparator;

public interface ITicker{

	public static final Comparator<ITicker> comparatorDailyChangePercent = new Comparator<ITicker>() {

		@Override
		public int compare(ITicker o1, ITicker o2) {
			Double d = o2.getDaylyChangePerCent();
			return d.compareTo(o1.getDaylyChangePerCent());
		}
	};
	String getShortName();

	String getName();

	double getVolumeDaily();

	double getHighDaily();

	double getLowDaily();

	double getLastPrice();

	double getDaylyChange();

	double getDaylyChangePerCent();

}