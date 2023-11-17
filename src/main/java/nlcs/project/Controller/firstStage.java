package nlcs.project.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import nlcs.project.Application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class firstStage implements Initializable {
    @FXML
    private AnchorPane bg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3),bg);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
        fadeTransition.setOnFinished(e->{
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("signup.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Welcome to myClothing");
                stage.setScene(scene);
                stage.show();
                bg.getScene().getWindow().hide();
            }catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("FINISH");
        });
    }
}
