package nlcs.project.Model;

public class Brand {
    private String Brand_ID;
    private String Brand_Name;
    private String Brand_Note;

    public Brand(String brandId, String brandName, String note) {
        Brand_ID = brandId;
        Brand_Name = brandName;
        Brand_Note = note;
    }
    public String getBrand_ID(){
        return Brand_ID;
    }
    public String getBrand_Name(){
        return Brand_Name;
    }
    public String getBrand_Note(){
        return Brand_Note;
    }
}
