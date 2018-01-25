package bg.panama.btc.trading.first;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import bg.panama.btc.model.Balance;
import bg.panama.btc.model.Balances;
import bg.panama.btc.model.v2.Tickers;
import bg.util.HibernateUtil;

public class BalancesFactory {

	
	EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
	public static BalancesFactory instance = new BalancesFactory();
	
	public void persists(Balances balances) {
		EntityManager em = emf.createEntityManager();
		persists(em, balances);
	}

	public void persists(EntityManager em, Balances balances)  {

		em.getTransaction().begin();
		em.persist(balances);
		em.getTransaction().commit();
	}

	public List<Balance> getBalance(int sizeMax, String currency, String type) {
		
		EntityManager em = emf.createEntityManager();
		String hql = "FROM Balance f WHERE f.currency =:currency  and  f.type=:type ORDER BY f.date DESC";// DESC
																						// ASC
		Query query = em.createQuery(hql);
		query.setParameter("currency", currency);
		query.setParameter("type", type);
		query.setMaxResults(120);
		List<Balance> list = (List<Balance>) query.getResultList();
		System.out.println("getBalance list.size : "+list.size()+" | type : "+type+"  | currency :"+currency);
		em.close();
		return list;

	}

}
