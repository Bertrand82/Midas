package bg.util;

public class PointTriple {

	
	public double x;
	public double y;
	public Object o1;
	public Object o2;
	public PointTriple(double x, double y, Object o1, Object o2) {
		super();
		this.x = x;
		this.y = y;
		this.o1 =o1;
		this.o2 = o2;
	}
	@Override
	public String toString() {
		return "P:[" + x + "," + y + "]";
	}
	
	
}
