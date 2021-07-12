import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerResources {

    @Id
    private String Name;
    private String Value;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public BitSellerResources() {

    }
}
