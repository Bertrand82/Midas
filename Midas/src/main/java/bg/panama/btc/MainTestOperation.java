package bg.panama.btc.model.operation;

import java.util.List;

import bg.panama.btc.BitfinexClient.OrderType;
import bg.panama.btc.model.operation.Operation;
import bg.panama.btc.model.operation.OperationEntitiesFactory;
import bg.panama.btc.model.operation.Order;

public class MainTestOperation {

	public static void main(String[] args) {
		insertOrder();
		insert();
		list();
	}
	public static void list() {
		System.out.println("MainTestOperation list start");
		List<Operation> list = OperationEntitiesFactory.instance.getOperations(10, Operation.PHASE_OP.test);
		System.out.println("MainTestOperation list size "+list.size());
		for(Operation ope : list){
			System.out.println(ope);
		}
	}
	
	public static void insert() {
		System.out.println("MainTestOperation start");
		Operation operation = new Operation();
		Order orderAchat = new Order("test", 10,Order.Side.buy, Order.TypeChoicePrice.manual);
		operation.setPhase(Operation.PHASE_OP.test);
		operation.setOrderAchat(orderAchat);
		OperationEntitiesFactory.instance.persists(operation);
		System.out.println("MainTestOperation done "+operation);
		
	}
	public static void insertOrder() {
		System.out.println("MainTestOperation order start");
		
		Order orderAchat = new Order("test", 10,Order.Side.buy, Order.TypeChoicePrice.manual);
		OperationEntitiesFactory.instance.persists(orderAchat);
		System.out.println("MainTestOperation order done");
		
		
	}

}
