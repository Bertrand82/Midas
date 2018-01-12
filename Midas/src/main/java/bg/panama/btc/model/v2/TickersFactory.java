package bg.panama.btc.model.v2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import bg.panama.btc.util.HibernateUtil;

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
		em.getTransaction().begin();
		em.persist(tickers);
		em.getTransaction().commit();
		em.close();
		
	}

}
