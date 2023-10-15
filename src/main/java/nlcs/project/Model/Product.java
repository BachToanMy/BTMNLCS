package nlcs.project.Model;

import java.sql.Statement;
import java.util.Date;

public class Product {
    private String Product_ID;
    private String Product_Name;
    private String Category_id;
    private String Brand_id;
    private Double Price_in;
    private Double Price_out;
    private String Category_name;
    private String Brand_name;
    private Integer Stock;
    private String Image;
    private Date date;
    private String Note;
    public Product(String product_ID,String product_Name,String category_id,String category_name,String brand_id,String brand_name,
                   Double price_in,Double price_out, Integer stock,String image,Date date,String note )
    {
        this.Product_ID = product_ID;
        this.Product_Name = product_Name;
        this.Category_id = category_id;
        this.Category_name = category_name;
        this.Brand_id = brand_id;
        this.Brand_name = brand_name;
        this.Price_in = price_in;
        this.Price_out = price_out;
        this.Stock = stock;
        this.Image = image;
        this.date = date;
        this.Note = note;
    }
    public String getProduct_ID(){
        return Product_ID;
    }
    public String getProduct_Name(){
        return  Product_Name;
    }
    public String getCategory_id(){
        return Category_id;
    }
    public String getCategory_name(){
        return Category_name;
    }
    public String getBrand_id(){
        return Brand_id;
    }
    public String getBrand_name(){
        return Brand_name;
    }
    public Double getPrice_in(){
        return Price_in;
    }
    public Double getPrice_out(){
        return Price_out;
    }
    public Integer getStock(){
        return Stock;
    }
    public String getImage(){
        return Image;
    }
    public Date getDate(){
        return date;
    }
    public String getNote(){
        return Note;
    }
}
