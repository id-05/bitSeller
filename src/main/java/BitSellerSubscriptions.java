import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerSubscriptions {
    private int id;
    private int user;
    private String tag;
    private int type;

    public void setId(int id) {
        this.id = id;
    }

    @Id
    public int getId() {
        return id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    BitSellerSubscriptions(){

    }

}
