import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerClients {

    @Id
    private String INN;
    private String Name;

    public BitSellerClients() {

    }

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    BitSellerClients(String Name, String INN){
        this.Name = Name;
        this.INN = INN;
    }
}
