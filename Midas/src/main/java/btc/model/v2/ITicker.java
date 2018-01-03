package btc.model.v2;

import java.io.Serializable;
import java.sql.Time;
import java.util.Comparator;
import java.util.Date;

public interface ITicker{

	public static final Comparator<ITicker> comparatorDailyChangePercent = new Comparator<ITicker>() {

		@Override
		public int compare(ITicker o1, ITicker o2) {
			Double d = o2.getDaylyChangePerCent();
			return d.compareTo(o1.getDaylyChangePerCent());
		}
	};
	public static final Comparator<ITicker> comparatorHourlyChangePercent = new Comparator<ITicker>() {

		@Override
		public int compare(ITicker o1, ITicker o2) {
			Double d = o2.getHourlyChangePerCent();
			return d.compareTo(o1.getHourlyChangePerCent());
		}
	};
	String getShortName();
	
	
	double getHourlyChangePerCent();
	/*Flash Return Rate - average of all fixed rate funding over the last hour
	 * 
	 */
	double getHourlyPrice() ;

	String getName();

	double getVolumeDaily();

	double getHighDaily();

	double getLowDaily();

	double getLastPrice();

	double getDaylyChange();

	double getDaylyChangePerCent();

	void setHourlyChangePerCent(double d);
	
	long getDeltaTemps_ms();
	
	void setDeltaTemps_ms(long delta);
	
    Date getDate();
    
    int getNumero();
    void setNumero(int number);

}