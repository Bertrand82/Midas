package bg.panama.btc.model.v2;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import bg.util.HibernateUtil;

public class TickersFactory {

	public static TickersFactory instance ;
	EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
	public static TickersFactory getInstance() {
		synchronized(TickersFactory.class){
			if (instance == null){
				instance = new TickersFactory();
			}
		}
		return instance;
	}
	public void persists(Tickers tickers) {
		EntityManager em = emf.createEntityManager();
		persists(em,tickers);
		em.close();
		
	}
	
	public void persists(EntityManager em,Tickers tickers) {
		em.getTransaction().begin();
		em.persist(tickers);
		em.getTransaction().commit();
	}
	public Tickers getLasTickers() {
		EntityManager em = emf.createEntityManager();
		try {			
			return getLasTickers(em);
		} finally {
			em.close();
		} 
	}
	public Tickers getLasTickers(EntityManager em) {
		List<Tickers> t = getLastTickers(1,em) ;
		if (t.size() > 0){
			return t.get(0);
		}
		return null;
	}
	
	public List<Tickers> getLastTickers(int n, EntityManager em) {
		 
		 String hql = "FROM Tickers f ORDER BY f.date DESC";// DESC  ASC
		 Query query = em.createQuery(hql);
		 query.setMaxResults(n);
		 List<Tickers> list =(List<Tickers>) query.getResultList();
		
		 return list;
		  
	}

}
