package nlcs.project.Model;

public class Account {
    private String idaccount;
    private   String username;
    private String password;
    public Account(){}

    public Account(String username){this.username=username;}

    public Account(String idaccount, String username) {
        this.username = username;
        this.idaccount = idaccount;
    }

    public String getUsername(){return username;}
    public String getIdaccount(){return idaccount;}
}
