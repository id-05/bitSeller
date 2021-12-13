import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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

    public default void saveNewGroup(BitSellerGroups group) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(group);
        transaction.commit();
        session.close();
    }

    public default void deleteGroup(BitSellerGroups group) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(group);
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

    public default boolean ifExistSubscription(BitSellerUsers user, String groupname) {
        String userId= user.getId();
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from BitSellerSubscriptions  where user=:userId and tag=:groupname";
        query = session.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("groupname", groupname);
        List<BitSellerSubscriptions> results = query.list();
        session.close();
        if (results.size() > 0) {
            return true;
        }else{
            return false;
        }
    }

    public default BitSellerSubscriptions getExistSubcription(BitSellerUsers user, String groupname) {
        String userId= user.getId();
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from BitSellerSubscriptions  where user=:userId and tag=:groupname";
        query = session.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("groupname", groupname);
        List<BitSellerSubscriptions> results = query.list();
        session.close();
        if (results.size() > 0) {
            Iterator<BitSellerSubscriptions> it = results.iterator();
            return (BitSellerSubscriptions) it.next();
        }else{
            BitSellerSubscriptions bufSub = new BitSellerSubscriptions();
            return bufSub;
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

    public default BitSellerGroups getGroupByName(String groupname){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerGroups U WHERE U.Name = '" + groupname + "'";
        Query query = session.createQuery(hql);
        List<BitSellerGroups> results = query.list();

        if (results.size() > 0) {
            Iterator<BitSellerGroups> it = results.iterator();
            return (BitSellerGroups) it.next();
        }else{
            BitSellerGroups group = new BitSellerGroups();
            return group;
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

    public default List<BitSellerClients> getAllClientsFromGroup(String groupname) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerClients U WHERE U.UGroup = '" + groupname + "'";
        Query query = session.createQuery(hql);
        List<BitSellerClients> results = query.list();
        return results;
    }

    public default List<BitSellerSubscriptions> getAllUserSubcriptions(BitSellerUsers user){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerSubscriptions U WHERE U.user = '" + user.getId() + "'";
        Query query = session.createQuery(hql);
        List<BitSellerSubscriptions> results = query.list();
        return results;
    }

    public default List<BitSellerSubscriptions> getAllSubcriptions(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitSellerSubscriptions> criteriaQuery = builder.createQuery(BitSellerSubscriptions.class);
        Root<BitSellerSubscriptions> root = criteriaQuery.from(BitSellerSubscriptions.class);
        criteriaQuery.select(root);
        Query<BitSellerSubscriptions> query = session.createQuery(criteriaQuery);
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

    public default List<BitSellerGroups> getAllGroups() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BitSellerGroups> criteriaQuery = builder.createQuery(BitSellerGroups.class);
        Root<BitSellerGroups> root = criteriaQuery.from(BitSellerGroups.class);
        criteriaQuery.select(root);
        Query<BitSellerGroups> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public default BitSellerClients getClientByINN(String clientINN){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "FROM BitSellerClients U WHERE U.id = '" + clientINN + "'";
        Query query = session.createQuery(hql);
        List<BitSellerClients> results = query.list();

        if (results.size() > 0) {
            Iterator<BitSellerClients> it = results.iterator();
            return  it.next();
        }else{
            return new BitSellerClients();
        }
    }

    public default void saveNewSubscription(BitSellerSubscriptions subscription) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(subscription);
        transaction.commit();
        session.close();
    }

    public default void deleteSubscription(BitSellerSubscriptions subscription) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(subscription);
        transaction.commit();
        session.close();
    }

    public default List<BitSellerSubscriptions> getUserSubscriptions(String userid) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        String hql= "from BitSellerSubscriptions  where user=:forgroup";
        query = session.createQuery(hql);
        query.setParameter("forgroup", userid);
        List<BitSellerSubscriptions> results = query.list();
        session.close();
        return results;
    }

}
