package nlcs.project.Model;

public class Item {
    private int Item_Cartid;
    private Double Item_price;
    private String Item_proid;
    private Integer Item_quantity;

    public Item(int cartId, String productId, int quantity, double price) {
        this.Item_Cartid=cartId;
        this.Item_proid = productId;
        this.Item_quantity =quantity;
        this.Item_price = price;
    }

    public int getItem_cartid(){return Item_Cartid;}

    public Double getItem_price() {
        return Item_price;
    }

    public Integer getItem_quantity() {
        return Item_quantity;
    }

    public String getItem_proid() {
        return Item_proid;
    }
}
