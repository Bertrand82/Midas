package bg.panama.btc.trading.first;

import javax.persistence.Embeddable;

@Embeddable
public class AmbianceMarket {
	
	private boolean isModePanic = false;
	
	private int nbPanic =0;
	private int nbPanicNo =0;
	
	public int getNbPanicNo() {
		return nbPanicNo;
	}

	public void setNbPanicNo(int nbPanicNo) {
		this.nbPanicNo = nbPanicNo;
	}

	private String causePanic ;
	
	public AmbianceMarket(){
		
	}

	public boolean isModePanic() {
		return isModePanic;
	}

	public void setModePanic(boolean isModePanic) {
		this.isModePanic = isModePanic;
		if (isModePanic){
			nbPanic++;
		}
	}

	public int getNbPanic() {
		return nbPanic;
	}

	public void setNbPanic(int nbPanic) {
		this.nbPanic = nbPanic;
	}

	public String getCausePanic() {
		return causePanic;
	}

	public void setCausePanic(String causePanic) {
		this.causePanic = causePanic;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		AmbianceMarket a = new AmbianceMarket();
		a.isModePanic = isModePanic;
		a.nbPanic = nbPanic;
		a.causePanic = causePanic;
		a.nbPanicNo = nbPanicNo;
		return a;
	}

	@Override
	public String toString() {
		return "AmbianceMarket |isModePanic=" + isModePanic + "| nbPanic=" + nbPanic + "| causePanic=" + causePanic;
	}
	
	
	

}