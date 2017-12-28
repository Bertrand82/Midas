package derby;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import btc.trading.first.Photo;

public class MainJPATest {
  private static final String PERSISTENCE_UNIT_NAME = "midas";
  private static EntityManagerFactory factory;

  public static void main(String[] args) {
	
	  
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();
    // Read the existing entries and write to console
   Query q = em.createQuery("select t from Photo t");
    List<Photo> photoList = q.getResultList();
    for (Photo photo : photoList) {
      System.out.println(photo);
    }
    System.out.println("Size: " + photoList.size());

    // Create new todo
    em.getTransaction().begin();
    Photo photo = new Photo();
    photo.setSummary("This is a test");
    photo.setDescription("This is a test");
    em.persist(photo);
    em.getTransaction().commit();

    em.close();
  }
} 