package nlcs.project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nlcs.project.Application;
import nlcs.project.Model.Account;
import nlcs.project.Model.database;

import java.io.IOException;
import java.sql.*;

public class signupcontroller {

    @FXML
    private AnchorPane signin_form;
    @FXML
    private TextField signinname_field;
    @FXML
    private TextField signinpass_field;
    @FXML
    private Button signinbtn;
    @FXML
    private Button signuptrans;
    @FXML
    private AnchorPane signup_form;
    @FXML
    private TextField cpassword_field;
    @FXML
    private TextField email_field;
    @FXML
    private TextField fullname_field;
    @FXML
    private TextField password_field;
    @FXML
    private TextField phone_field;
    @FXML
    private TextField username_field;
    @FXML
    private Button signupbtn;
    @FXML
    private Button signintrans;
    @FXML
    private Button return_btn;
    @FXML
    private Button returnbtn;
    private Alert alert;

    private database db = new database();
    private Connection connection = db.getConnection();
    private PreparedStatement preparedStatement;
    private ResultSet result;
    private Statement statement;

    public Account account = new Account();


    public void switchForm(ActionEvent event){
        if(event.getSource() == signuptrans){
            signin_form.setVisible(false);
            signup_form.setVisible(true);
        }
        if (event.getSource() == signintrans) {
            signin_form.setVisible(true);
            signup_form.setVisible(false);
        }
    }

    public void returnbtn(ActionEvent event) throws IOException {
        if(event.getSource()==returnbtn) {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("firstStage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            returnbtn.getScene().getWindow().hide();
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("firstStage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            return_btn.getScene().getWindow().hide();
        }

    }

    public void signup(ActionEvent event){
        if(fullname_field.getText().isEmpty()||
            phone_field.getText().isEmpty()||
            email_field.getText().isEmpty()||
            username_field.getText().isEmpty()||
            password_field.getText().isEmpty()||
            cpassword_field.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else if(phone_field.getText().length()!=10){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Phone number must be ten numbers");
            alert.showAndWait();
        }else if(!password_field.getText().equals(cpassword_field.getText())){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Confirm password not equals to password");
            alert.showAndWait();
        } else {
            String checkusername="SELECT username FROM account WHERE username = '"
                    + username_field.getText() + "'";
            try{
                statement = connection.createStatement();
                result = statement.executeQuery(checkusername);
                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(username_field.getText() + "is already taken");
                    alert.showAndWait();
                }else{
                    String insertUser = "INSERT INTO user "
                            + "(fullname,phone,email,date)"
                            + "VALUE(?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insertUser);
                    preparedStatement.setString(1,fullname_field.getText());
                    preparedStatement.setString(2,phone_field.getText());
                    preparedStatement.setString(3,email_field.getText());
                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new  java.sql.Date(date.getTime());;
                    preparedStatement.setString(4,String.valueOf(sqlDate));

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Add User");
                    alert.showAndWait();

                    String getiduser = "SELECT MAX(iduser) FROM user";
                    preparedStatement = connection.prepareStatement(getiduser);
                    result = preparedStatement.executeQuery();
                    if(result.next()){
                        Integer iduser = result.getInt("MAX(iduser)");
                        String insertAccount = "INSERT INTO account "
                                + "(iduser,Username,Password,date)"
                                + "VALUE(?,?,?,?)";
                        preparedStatement = connection.prepareStatement(insertAccount);
                        preparedStatement.setString(1,iduser.toString());
                        preparedStatement.setString(2,username_field.getText());
                        preparedStatement.setString(3,password_field.getText());
                        java.util.Date date1 = new java.util.Date();
                        java.sql.Date sqlDate1 = new  java.sql.Date(date1.getTime());;
                        preparedStatement.setString(4,String.valueOf(sqlDate1));

                        preparedStatement.executeUpdate();
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Add Account");
                        alert.showAndWait();
                        signin_form.setVisible(true);
                        signup_form.setVisible(false);
                    }

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void signin(ActionEvent event){
        if(signinname_field.getText().isEmpty() || signinpass_field.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill Username, Password");
            alert.show();
        }else{
            try {
                preparedStatement  = connection.prepareStatement("select * from account where Username=? and Password=?");
                String uname = signinname_field.getText();
                String pass = signinpass_field.getText();
                preparedStatement.setString(1,uname);
                preparedStatement.setString(2,pass);

                result = preparedStatement.executeQuery();

                if(result.next()){
                    //TO GET THE USERNAME THAT USER USED
                    Account.username = signinname_field.getText();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successfully");
                    alert.showAndWait();
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setTitle("Welcome to myClothing");
                    stage.setScene(scene);
                    stage.show();
                    signinbtn.getScene().getWindow().hide();
                }
                else {
                    alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username or Password");
                    alert.showAndWait();
                    signinname_field.setText("");
                    signinpass_field.setText("");
                }

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


}
