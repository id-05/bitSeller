import javax.naming.Name;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerGroups {

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Id
    private String Name;

    public BitSellerGroups(){

    }

    public BitSellerGroups(String Name){
        this.Name = Name;
    }

}
