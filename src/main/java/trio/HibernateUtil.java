package trio;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public final class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private HibernateUtil() {
	}
	
	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static void shutdown() {
		getSessionFactory().close();
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}