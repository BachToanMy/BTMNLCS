package nlcs.project.Model;
import java.sql.*;
public class database {
    Connection connection;
    String URL = "jdbc:mysql://localhost/nlcs";
    String user = "admin";
    String password = "";
    public database(){}
    public void connect1(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(URL,"root","");
            System.out.println("Connect Successfully...");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connect faillllllllll");
            e.printStackTrace();
        }

    }

    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(URL,"root","");
            System.out.println("Connect Successfully...");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connect faillllllllll");
            e.printStackTrace();
        }
        return connection;
    }
}
