package btc.model.v2;

public interface ITicker {

	String getShortName();

	String getName();

	double getVolumeDaily();

	double getHighDaily();

	double getLowDaily();

	double getLastPrice();

	double getDaylyChange();

	double getDaylyChangePerCent();

}