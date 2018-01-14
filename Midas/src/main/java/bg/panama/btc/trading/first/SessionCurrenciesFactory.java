package bg.panama.btc.trading.first;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.model.v2.Tickers;
import bg.util.HibernateUtil;



public class SessionCurrenciesFactory {
	EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
	public static SessionCurrenciesFactory instance = new SessionCurrenciesFactory();

	public  void persists(SessionCurrencies sessionCurrencies) {
		// TODO Auto-generated method stub
		
	}

	public  void persists(EntityManager em, SessionCurrencies sessionCurrencies, Tickers tickers) throws Exception{
		
		em.getTransaction().begin();
		em.persist(tickers);
		em.persist(sessionCurrencies);
		em.getTransaction().commit();
	}

	public List<SessionCurrency> getSessionsCurrency(int n, String name) {
		 EntityManager em = emf.createEntityManager();
		 String hql = "FROM SessionCurrency f WHERE f.name =:name  ORDER BY f.date DESC";// DESC  ASC
		 Query query = em.createQuery(hql);
		 query.setParameter("name", name);
		 query.setMaxResults(120);
		 List<SessionCurrency> list =(List<SessionCurrency>) query.getResultList();
		 
		 em.close();
		 return list;
		  
		
	}

	public SessionCurrencies getLastSessionCurrencies() {
		EntityManager em = emf.createEntityManager();
		String hql = "FROM SessionCurrencies f   ORDER BY f.id DESC";// DESC  ASC
		 Query query = em.createQuery(hql);
		 query.setMaxResults(1);
		 List<SessionCurrencies> list =(List<SessionCurrencies>) query.getResultList();
		 if (list .isEmpty()){
			 return null;
		 }
		 SessionCurrencies sc = list.get(0);
		 em.close();
		return sc;
	}

	
	

}
