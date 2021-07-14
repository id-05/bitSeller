public class Purchase {
    String id;
    String INN;
    String description;
    String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public Purchase(String INN, String id, String description, String price){
        this.INN = INN;
        this.id = id;
        this.description = description;
        this.price = price;
    }
}