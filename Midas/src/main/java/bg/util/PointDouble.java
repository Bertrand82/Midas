package bg.util;

public class PointDouble {

	
	public double x;
	public double y;
	public PointDouble(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() {
		return "P:[" + x + "," + y + "]";
	}
	
	
}
