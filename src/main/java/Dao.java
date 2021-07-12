import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Iterator;
import java.util.List;

public interface Dao {

    public default void initialHibernate(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

    public default String getBitSellerResource(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerResources U WHERE U.Name = '" + name + "'";
        Query query = session.createQuery(hql);
        List<BitSellerResources> results = query.list();

        if (results.size() > 0) {
            Iterator<BitSellerResources> it = results.iterator();
            return  (String) it.next().getValue();
        }
        return "null";
    }

    public default void saveNewUser(BitSellerUsers user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }

    public default void deleteUser(BitSellerUsers user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    public default void saveNewClient(BitSellerClients client) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(client);
        transaction.commit();
        session.close();
    }

    public default void deleteClient(BitSellerClients client) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(client);
        transaction.commit();
        session.close();
    }

    public default void saveNewPurchase(BitSellerPurchase purchase) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(purchase);
        transaction.commit();
        session.close();
    }

    public default void deletePurchase(BitSellerPurchase purchase) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(purchase);
        transaction.commit();
        session.close();
    }

    public default boolean validateUser(String userid) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerUsers U WHERE U.id = '" + userid +"'";
        Query query = session.createQuery(hql);
        List<BitSellerUsers> results = query.list();

        if (results.size() > 0) {
            return true;
        }else{
            return false;
        }
    }

    public default BitSellerUsers getUserById(String userid){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerUsers U WHERE U.id = '" + userid + "'";
        Query query = session.createQuery(hql);
        List<BitSellerUsers> results = query.list();

        if (results.size() > 0) {
            Iterator<BitSellerUsers> it = results.iterator();
            return (BitSellerUsers) it.next();
        }else{
            BitSellerUsers user = new BitSellerUsers();
            return user;
        }
    }

}
