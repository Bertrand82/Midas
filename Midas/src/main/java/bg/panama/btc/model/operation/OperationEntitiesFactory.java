package bg.panama.btc.model.operation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import bg.panama.btc.trading.first.SessionCurrency;
import bg.util.HibernateUtil;

public class OperationEntitiesFactory {

	
	EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
	public static OperationEntitiesFactory instance = new OperationEntitiesFactory();

	public void persists(Operation operation) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(operation.getOrderAchat());
		em.persist(operation);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Operation> getOperations(int n,Operation.PHASE_OP phase ) {
		EntityManager em = emf.createEntityManager();
		String hql = "FROM Operation f WHERE f.phase =:phase  ORDER BY f.date DESC";// DESC
																						// ASC
		Query query = em.createQuery(hql);
		query.setParameter("phase", phase);
		query.setMaxResults(n);
		List<Operation> list = (List<Operation>) query.getResultList();
		em.close();
		return list;

	}

	public List<Operation> getListOperationInit() {
		
		try {
			EntityManager em = emf.createEntityManager();
			String hql = "FROM Operation f WHERE f.phase !=:phase  ORDER BY f.date DESC";// DESC
																							// ASC
			Query query = em.createQuery(hql);
			query.setParameter("phase", Operation.PHASE_OP.closed);
			
			List<Operation> list = (List<Operation>) query.getResultList();
			em.close();
			System.err.println("list Order no closed "+list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void persists(Order order) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(order);
		em.getTransaction().commit();
		em.close();
	}


}
