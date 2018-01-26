package bg.panama.btc.trading.first;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import bg.panama.btc.model.Balance;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.v2.Tickers;
import bg.util.HibernateUtil;

public class OrderPersistFactory {

	
	EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
	public static OrderPersistFactory instance = new OrderPersistFactory();
	
	public void persists(Order order) {
		EntityManager em = emf.createEntityManager();
		persists(em, order);
	}
	
	public void persists(List<Order> orders) {
		if (orders == null){
			return;
		}
		if (orders.isEmpty()){
			return;
		}
		EntityManager em = emf.createEntityManager();
		persists(em, orders);
	}
	public void persists(EntityManager em, List<Order> orders)  {

		em.getTransaction().begin();
		for(Order o: orders ){
			em.persist(o);
		}
		em.getTransaction().commit();
	}

	public void persists(EntityManager em, Order order)  {

		em.getTransaction().begin();
		em.persist(order);
		em.getTransaction().commit();
		em.close();
	}

	public List<Order> getOrders(int sizeMax, String currency) {
		
		EntityManager em = emf.createEntityManager();
		String hql = "FROM Order f WHERE f.currency =:currency  ORDER BY f.date DESC";// DESC
																						// ASC
		Query query = em.createQuery(hql);
		query.setParameter("currency", currency);
		query.setMaxResults(120);
		List<Order> list = (List<Order>) query.getResultList();
		System.out.println("getOrders list.size : "+list.size()+"   | currency :"+currency);
		em.close();
		return list;

	}

	public void merge(Order order) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(order);
		em.getTransaction().commit();
	}



}
