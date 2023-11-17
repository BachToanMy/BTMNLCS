package nlcs.project.Model;

public class User {
    private String fullname;
    private String phone;
    private String email;

    private Integer iduser;

    private Integer idaccount;
    private String username;

    public User(Integer iduser, String fullname, String phone,String email,Integer idaccount,String username){
        this.iduser = iduser;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.idaccount = idaccount;
        this.username = username;
    }

    public Integer getIduser(){
        return iduser;
    }

    public String getFullname(){
        return fullname;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }

    public Integer getIdaccount(){
        return idaccount;
    }

    public String getUsername(){
        return username;
    }
}
