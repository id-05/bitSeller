package DAO;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerSubscriptions {
    private int id;
    private String user;
    private String tag;

    public void setId(int id) {
        this.id = id;
    }

    @Id
    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BitSellerSubscriptions() {

    }

    public BitSellerSubscriptions(String user, String tag) {
        this.user = user;
        this.tag = tag;
    }

}
