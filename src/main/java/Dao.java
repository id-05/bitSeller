import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public default boolean ifExistPurchase(String purchaseid) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerPurchase U WHERE U.purchaseid = '" + purchaseid +"'";
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

    public default List<BitSellerClients> getAllClients() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitSellerClients> criteriaQuery = builder.createQuery(BitSellerClients.class);
        Root<BitSellerClients> root = criteriaQuery.from(BitSellerClients.class);
        criteriaQuery.select(root);
        Query<BitSellerClients> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public default List<BitSellerUsers> getAllUsers() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitSellerUsers> criteriaQuery = builder.createQuery(BitSellerUsers.class);
        Root<BitSellerUsers> root = criteriaQuery.from(BitSellerUsers.class);
        criteriaQuery.select(root);
        Query<BitSellerUsers> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public default String getClientNameByINN(String clientINN){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerClients U WHERE U.id = '" + clientINN + "'";
        Query query = session.createQuery(hql);
        List<BitSellerClients> results = query.list();

        if (results.size() > 0) {
            Iterator<BitSellerClients> it = results.iterator();
            return  it.next().getName();
        }else{
            return "noname";
        }
    }

}
