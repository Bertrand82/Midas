package bg.panama.btc.trading.commun;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Value {

	
	public Value() {
	}

	@Id
	@GeneratedValue
	private long id ;
	public long time = System.currentTimeMillis();
	public double v=0;
	private double v_moyenne=0;
	private double derivee=0;
	private long id__Z_1=0;
	long dt =5*600000;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getV() {
		return v;
	}
	public void setV(double v) {
		this.v = v;
	}
	public double getV_moyenne() {
		return v_moyenne;
	}
	public void setV_moyenne(double v_moyenne) {
		this.v_moyenne = v_moyenne;
	}
	public double getDerivee() {
		return derivee;
	}
	public void setDerivee(double derivee) {
		this.derivee = derivee;
	}
	public long getId__Z_1() {
		return id__Z_1;
	}
	public void setId__Z_1(long id__Z_1) {
		this.id__Z_1 = id__Z_1;
	}
	public void process(Value z_1) {
		if (z_1 == null){
			return;
		}
		this.id__Z_1 = z_1.id;
		long dt1 = time-z_1.time;
		this.v_moyenne = (dt * z_1.v_moyenne +dt1* v)/(dt1+dt);
		this.derivee= (v - z_1.v_moyenne)/dt1;
	}
	
	public Object clone() {
		Value val = new Value();
		val.id = this.id;
		val.id__Z_1 = this.id__Z_1;
		val.v = this.v;
		val.v_moyenne = this.v_moyenne;
		val.derivee = this.derivee;
		return val;
	}
	@Override
	public String toString() {
		return "Value [v=" + v + ", v_moyenne=" + v_moyenne + ", derivee=" + derivee + "]";
	}
	
	
}
