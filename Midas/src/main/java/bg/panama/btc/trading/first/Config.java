package bg.panama.btc.trading.first;

import java.io.Serializable;

public class Config implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean orderAble = false;
	
	private transient String password =null;
	private static Config instance;
	public Config(boolean orderAble, String password) {
		super();
		this.orderAble = orderAble;
		this.password = password;
		instance =this;
	}
	public boolean isOrderAble() {
		return orderAble;
	}
	public void setOrderAble(boolean orderAble) {
		this.orderAble = orderAble;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static Config getInstance() {
		return instance;
	}

	
}
