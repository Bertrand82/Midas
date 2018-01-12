package bg.panama.btc.trading.first;

import java.io.Serializable;

public class Config implements Serializable{

	private static final long serialVersionUID = 1L;
	boolean orderAble = false;
	String password ="";
	public Config(boolean orderAble, String password) {
		super();
		this.orderAble = orderAble;
		this.password = password;
	}
	public boolean isOrderAble() {
		return orderAble;
	}
	public void setOrderAble(boolean orderAble) {
		this.orderAble = orderAble;
	}

}
