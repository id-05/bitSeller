import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerClients {

    @Id
    private String INN;
    private String Name;
    private String UGroup;

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

    public String getUGroup() {
        return UGroup;
    }

    public void setUGroup(String UGroup) {
        this.UGroup = UGroup;
    }

    public BitSellerClients() {

    }

    BitSellerClients(String Name, String INN){
        this.Name = Name;
        this.INN = INN;
    }

    BitSellerClients(String Name, String INN, String UGroup){
        this.Name = Name;
        this.INN = INN;
        this.UGroup = UGroup;
    }
}
