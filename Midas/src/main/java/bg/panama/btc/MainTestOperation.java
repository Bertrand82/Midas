package bg.panama.btc;

import java.util.List;

import bg.panama.btc.model.operation.Operation;
import bg.panama.btc.model.operation.OperationFactory;
import bg.panama.btc.trading.first.Order;

public class MainTestOperation {

	public static void main(String[] args) {
		insert();
		list();
	}
	public static void list() {
		System.out.println("MainTestOperation list start");
		List<Operation> list = OperationFactory.instance.getOperations(10, Operation.PHASE_OP.test);
		System.out.println("MainTestOperation list size "+list.size());
		for(Operation ope : list){
			System.out.println(ope);
		}
	}
	
	public static void insert() {
		System.out.println("MainTestOperation start");
		Operation operation = new Operation();
		Order orderAchat = new Order("test", 10, Order.Side.buy, Order.TypeChoicePrice.fromBookOrder);
		operation.setPhase(Operation.PHASE_OP.test);
		operation.setOrderAchat(orderAchat);
		OperationFactory.instance.persists(operation);
		System.out.println("MainTestOperation done "+operation);
		
	}

}
