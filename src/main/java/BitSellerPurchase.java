import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerPurchase {

    @Id
    public String purchaseid;
    public String clientinn;

    public String getPurchaseid() {
        return purchaseid;
    }

    public void setPurchaseid(String purchaseid) {
        this.purchaseid = purchaseid;
    }

    public String getClientinn() {
        return clientinn;
    }

    public void setClientid(String clientinn) {
        this.clientinn = clientinn;
    }

    public BitSellerPurchase(){

    }

    public BitSellerPurchase(String purchaseid, String clientinn){
        this.purchaseid = purchaseid;
        this.clientinn = clientinn;
    }
}
