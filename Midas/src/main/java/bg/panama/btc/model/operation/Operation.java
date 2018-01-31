package bg.panama.btc.model.operation;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import bg.panama.btc.OrderManager;
import bg.panama.btc.model.operation.Order.Side;
import bg.panama.btc.model.operation.Order.TypeChoicePrice;

@Entity
@Table(name="OPERATION_BTFX3")

public class Operation {
	
	public static enum PHASE_OP{Preparation,buying,started, running, closing, closed, error, test};
	
	public Operation() {
	}
	
	public Operation(String currency, double amount,  TypeChoicePrice type) {
		this.orderAchat=new Order(currency, amount, Order.Side.buy,Order.TypeChoicePrice.fromTickers);
		OperationsManager.instance.addOperation(this);
	}

	@Id
	@GeneratedValue
	private long id;
	
	private Date date = new Date();
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="operation")
	private Order orderAchat;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id")
	private Order orderVenteStop;
		
	private PHASE_OP phase  = PHASE_OP.Preparation;
	private double stop;
	private double limit;
	
	@Override
	public String toString() {
		return "Operation [id=" + id + ", date=" + date + ", orderAchat=[" + orderAchat + "] , orderVenteStop="
				+ orderVenteStop + ", orderVenteLimit=" + limit + ", phase=" + phase + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Order getOrderAchat() {
		return orderAchat;
	}
	public void setOrderAchat(Order orderAchat) {
		this.orderAchat = orderAchat;
		
	}
	public Order getOrderVenteStop() {
		return orderVenteStop;
	}
	public void setOrderVenteStop(Order orderVenteStop) {
		this.orderVenteStop = orderVenteStop;
	}
	
	public PHASE_OP getPhase() {
		return phase;
	}
	public void setPhase(PHASE_OP phase) {
		this.phase = phase;
	}
	public double getLimit() {
		return limit;
	}
	public void setLimit(double limit) {
		this.limit = limit;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void achat() {
		OrderManager.getInstance().sendOrderPrivate(this.orderAchat);
		this.phase = PHASE_OP.buying;
	}

	

}
