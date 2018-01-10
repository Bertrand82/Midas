package btc.util;



import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



public class HibernateUtil {

    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
        	EntityManagerFactory emf = Persistence.createEntityManagerFactory("manager1");
            return emf;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void shutdown() {
    	// Close caches and connection pools
    	getEntityManagerFactory().close();
    }

}