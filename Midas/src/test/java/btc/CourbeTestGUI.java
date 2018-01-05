package btc;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class testGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	JFrame frame = new JFrame();
	int w=420;
	int h=60;
	Dimension dim = new Dimension(w, h);
	List<PointBg> listP = new ArrayList<testGUI.PointBg>();
	int imax = 1000;
	public testGUI() {
		setPreferredSize(dim);
		for(int i=0; i<imax;i++){
			double x=i/100.0;
			double y = Math.cos(x) ;
			listP.add(new PointBg(x, y));
		}
		initMinMax();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.black);
		g.drawLine(0, h/2, w, h/2);
		g.setColor(Color.red);
		for(PointBg p : listP){		
			int x =(int)((( p.x/dx) )*w);
			int y =(int) ((-p.y +yMax)/dy * h );
			g.fillOval(x, y, 4, 4);
		}		
	}
	public static void main(String[] args) {
		System.out.println("MainTest");
	  new testGUI();
	}

	Double xMax;
	Double xMin;
	Double yMax;
	Double yMin;
	double xM ;
	double yM ;
	double dx;
	double dy;
	void initMinMax(){		
		for(PointBg p : listP){
			initMinMax(p);
		}
		System.err.println(" xMax"+xMax+" xMin"+xMin+" xM "+xM);
	}
	void initMinMax(PointBg p){
		if(xMax == null){
			xMax = p.x;
			xMin = p.x;
			yMax = p.y;
			yMin =p.y;
		}
		if(p.x > xMax){
			xMax= p.x;
		}
		if(p.y > yMax){
			yMax= p.y;
		}
		if(p.x < xMin){
			xMin= p.x;
		}
		if(p.y < yMin){
			yMin= p.y;
		}
		
		dx = xMax - xMin;
		dy = yMax - yMin;
		xM = (xMax+xMin)/2;
		yM = (yMax+yMin)/2;
	}
	class PointBg {
		double x;
		double y;
		PointBg(double x, double y){
			this.x=x;
			this.y = y;
		}
	}
	

}
