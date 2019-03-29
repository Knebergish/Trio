package trio;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import trio.model.field.FieldImpl;
import trio.model.game.GameImpl;
import trio.model.gamer.GamerImpl;


public final class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private HibernateUtil() {
	}
	
	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			
			configuration.setProperty("hibernate.connection.url", System.getenv("DB_URL"));
			configuration.setProperty("hibernate.connection.username", System.getenv("DB_USER"));
			configuration.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
			configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
			configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
			configuration.setProperty("hibernate.show_sql", "true");
			configuration.setProperty("hibernate.format_sql", "true");
			configuration.setProperty("hibernate.connection.pool_size", "2");
			configuration.setProperty("hibernate.current_session_context_class", "thread");
			
			configuration.setProperty("hibernate.c3p0.acquire_increment", "1");
			configuration.setProperty("hibernate.c3p0.idle_test_period", "60");
			configuration.setProperty("hibernate.c3p0.min_size", "1");
			configuration.setProperty("hibernate.c3p0.max_size", "2");
			configuration.setProperty("hibernate.c3p0.max_statements", "50");
			configuration.setProperty("hibernate.c3p0.timeout", "0");
			configuration.setProperty("hibernate.c3p0.acquireRetryAttempts", "1");
			configuration.setProperty("hibernate.c3p0.acquireRetryDelay", "250");
			configuration.setProperty("hibernate.c3p0.testConnectionOnCheckout", "true");
			
			configuration.addAnnotatedClass(GamerImpl.class);
			configuration.addAnnotatedClass(GameImpl.class);
			configuration.addAnnotatedClass(FieldImpl.class);
			
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			return configuration.buildSessionFactory(serviceRegistry);
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