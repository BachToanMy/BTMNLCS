package nlcs.project.Model;

public class Receipt {
    private int cart_id;
    private Double total;
    public Receipt(int cart_id, Double tt){
        this.cart_id=cart_id;
        this.total=tt;
    }
    public Double getTotal(){
        return total;
    }
    public int getCart_id(){return cart_id;}
}
