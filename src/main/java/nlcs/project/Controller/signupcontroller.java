package nlcs.project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nlcs.project.Application;
import nlcs.project.Model.Account;
import nlcs.project.Model.database;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

public class signupcontroller implements Initializable {



    public String hashString(String string) {
        return DigestUtils.sha256Hex(string);
    }
    @FXML
    private AnchorPane signin_form;
    @FXML
    private TextField signinname_field;
    @FXML
    private PasswordField signinpass_field;
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
    private AnchorPane changePass_form;
    private Alert alert;

    private database db = new database();
    private Connection connection = db.getConnection();
    private PreparedStatement preparedStatement;
    private ResultSet result;
    private Statement statement;

    @FXML private Button signuptrans1;
    @FXML private Button signinbtn1;
    @FXML private Hyperlink changepassbtn;

    public Account account = new Account();


    public void switchForm(ActionEvent event){
        if(event.getSource() == signuptrans || event.getSource()==signuptrans1){
            signin_form.setVisible(false);
            signup_form.setVisible(true);
            changePass_form.setVisible(false);
        } else if (event.getSource() == signintrans|| event.getSource()==signinbtn1) {
            signin_form.setVisible(true);
            signup_form.setVisible(false);
            changePass_form.setVisible(false);
        } else if(event.getSource() == changepassbtn){
            signin_form.setVisible(false);
            signup_form.setVisible(false);
            changePass_form.setVisible(true);
        }else {
            signin_form.setVisible(true);
            signup_form.setVisible(false);
            changePass_form.setVisible(false);
        }
    }


    public void signup() {
        if (fullname_field.getText().isEmpty() ||
                phone_field.getText().isEmpty() ||
                email_field.getText().isEmpty() ||
                username_field.getText().isEmpty() ||
                password_field.getText().isEmpty() ||
                cpassword_field.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else if (phone_field.getText().length() != 10) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Phone number must be ten numbers");
            alert.showAndWait();
        } else if (!password_field.getText().equals(cpassword_field.getText())) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Confirm password not equals to password");
            alert.showAndWait();
        } else {
            String checkusername = "SELECT username FROM account WHERE username = '"
                    + username_field.getText() + "'";
            try {
                statement = connection.createStatement();
                result = statement.executeQuery(checkusername);
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(username_field.getText() + "is already taken");
                    alert.showAndWait();
                } else {
                    String insertAccount = "INSERT INTO account "
                            + "(Username,Password,date)"
                            + "VALUE(?,?,?)";
                    preparedStatement = connection.prepareStatement(insertAccount);
                    preparedStatement.setString(1, username_field.getText());
                    preparedStatement.setString(2, hashString(password_field.getText()));
                    java.util.Date date1 = new java.util.Date();
                    java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());
                    ;
                    preparedStatement.setString(3, String.valueOf(sqlDate1));
                    preparedStatement.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Add Account");
                    alert.showAndWait();
                    String getid = "SELECT MAX(idaccount) FROM account";
                    preparedStatement = connection.prepareStatement(getid);
                    result = preparedStatement.executeQuery();
                    if (result.next()) {
                        int id = result.getInt("MAX(idaccount)");
                        String insertUser = "INSERT INTO user "
                                + "(fullname,phone,email,date,idaccount)"
                                + "VALUE(?,?,?,?,?)";
                        preparedStatement = connection.prepareStatement(insertUser);
                        preparedStatement.setString(1, fullname_field.getText());
                        preparedStatement.setString(2, phone_field.getText());
                        preparedStatement.setString(3, email_field.getText());
                        java.util.Date date = new java.util.Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        ;
                        preparedStatement.setString(4, String.valueOf(sqlDate));
                        preparedStatement.setInt(5, id);

                        preparedStatement.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Add User");
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
    public void signin(){
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
                preparedStatement.setString(2,hashString(pass));

                result = preparedStatement.executeQuery();

                if(result.next()){
                    //TO GET THE USERNAME THAT USER USED
//                    Account.username = signinname_field.getText();
                    Account account1 = new Account(result.getString("idaccount"),signinname_field.getText());
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successfully");
                    alert.showAndWait();
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load());
                    Mainview Mainviewcontroller = fxmlLoader.<Mainview>getController();
                    Mainviewcontroller.setData(account1);
                    stage.setTitle("Welcome to myClothing");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setFullScreen(true);
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
    @FXML private TextField change_username;
    @FXML private TextField change_oldpass;
    @FXML private TextField change_newpass;
    public void changePassword(){
        String sql = "UPDATE account SET Password=? WHERE username=? and Password=?";
        if(change_username.getText().isEmpty() || change_oldpass.getText().isEmpty()||change_newpass.getText().isEmpty()){
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill out all fields");
            alert.showAndWait();
        }else{
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,hashString(change_newpass.getText()));
                preparedStatement.setString(2,change_username.getText());
                preparedStatement.setString(3,hashString(change_oldpass.getText()));
                preparedStatement.executeUpdate();
                alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Change password successfully");
                alert.showAndWait();
            } catch (SQLException e) {
                alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password");
                alert.showAndWait();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signin_form.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signin();
            }
        });
        signup_form.setOnKeyPressed(keyEvent ->{
            if(keyEvent.getCode() == KeyCode.ENTER){
                signup();
            }
        } );
        changePass_form.setOnKeyPressed(keyEvent ->{
            if(keyEvent.getCode() == KeyCode.ENTER){
                changePassword();
            }
        } );

    }
}
