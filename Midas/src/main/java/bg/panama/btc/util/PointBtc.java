package bg.panama.btc.util;

public class PointBtc {

	
	public double x;
	public double y;
	public PointBtc(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() {
		return "P:[" + x + "," + y + "]";
	}
	
	
}
