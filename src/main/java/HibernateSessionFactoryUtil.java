
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;
    //public static StringBuilder stringBuilder = new StringBuilder();

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(BitSellerClients.class);
                configuration.addAnnotatedClass(BitSellerUsers.class);
                configuration.addAnnotatedClass(BitSellerResources.class);
                configuration.addAnnotatedClass(BitSellerPurchase.class);
                configuration.addAnnotatedClass(BitSellerGroups.class);
                configuration.addAnnotatedClass(BitSellerSubscriptions.class);
                Properties properties = new Properties();
                properties.setProperty("hibernate.connection.driver_class", Main.hibernateSettings.gethDriver());
                properties.setProperty("hibernate.connection.url","jdbc:mysql://"+
                        Main.hibernateSettings.gethIP()+":"+Main.hibernateSettings.gethPort()+"/"
                        +Main.hibernateSettings.gethBaseName()+"?serverTimezone=UTC");
                properties.setProperty("hibernate.connection.username",Main.hibernateSettings.gethLogin());
                properties.setProperty("hibernate.connection.password",Main.hibernateSettings.gethPassword());
                properties.setProperty("hibernate.dialect",Main.hibernateSettings.gethDialect());
                configuration.addProperties(properties);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        return sessionFactory;
    }
}


