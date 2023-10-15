package nlcs.project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nlcs.project.Application;

import java.io.IOException;

public class firstStage {
    @FXML
    private AnchorPane bg;

    @FXML
    private Button signinbtn;

    @FXML
    private Button signupbtn;
    private signupcontroller signupcontroller;
    public void changeStage(ActionEvent event) throws IOException {
        if(event.getSource()==signinbtn){
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("signup.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Welcome to myClothing");
            stage.setScene(scene);
            stage.show();
            signinbtn.getScene().getWindow().hide();
        } else{
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("signupsignin.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Welcome to myClothing");
            stage.setScene(scene);
            stage.show();
            signupbtn.getScene().getWindow().hide();
        }
    }
}
