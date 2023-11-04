package nlcs.project.Model;

import java.sql.Date;

public class Receipt {
    private int receipt_id;
    private int discount;
    private double finaltotal;
    private double paid;
    private String note;
    private Date date;
    private int cart_id;
    private Double total;
    public Receipt(int cart_id, Double tt){
        this.cart_id=cart_id;
        this.total=tt;
    }

    public Receipt(int receiptId, int cartId, double total, int discount, double finaltotal, double paid, Date date, String note) {
        this.receipt_id = receiptId;
        this.cart_id = cartId;
        this.total=total;
        this.discount=discount;
        this.finaltotal=finaltotal;
        this.paid=paid;
        this.note=note;
        this.date=date;
    }

    public Double getTotal(){
        return total;
    }
    public int getCart_id(){return cart_id;}
    public int getReceipt_id(){return receipt_id;}
    public int getDiscount(){return discount;}
    public double getFinaltotal(){return finaltotal;}
    public double getPaid(){return paid;}
    public Date getDate(){return  date;}
    public String getNote(){return note;}
}
