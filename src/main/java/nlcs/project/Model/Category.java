package nlcs.project.Model;

public class Category {
    private String Category_ID;
    private String Category_Name;
    private String Category_Note;
    public Category(String Cateid,String Catename,String catenote){
        this.Category_ID = Cateid;
        this.Category_Name = Catename;
        this.Category_Note=catenote;
    }
    public String getCategory_ID(){
        return Category_ID;
    }
    public String getCategory_Name(){
        return Category_Name;
    }
    public String getCategory_Note(){
        return Category_Note;
    }
}
