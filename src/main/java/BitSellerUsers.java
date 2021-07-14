import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerUsers {

    @Id
    private String id;
    private String Name;
    private boolean subscription;

    public BitSellerUsers() {

    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public BitSellerUsers(String id, String Name) {
        this.id = id;
        this.Name = Name;
    }
}
