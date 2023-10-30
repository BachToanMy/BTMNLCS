package nlcs.project.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import nlcs.project.Application;
import nlcs.project.Model.Category;
import nlcs.project.Model.database;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryForm {
    @FXML
    private Button Cateform_cancel;

    @FXML
    private Button Cateform_clear;

    @FXML
    private TextField Cateform_id;

    @FXML
    public static Label Cateform_label;

    @FXML
    private TextField Cateform_name;

    @FXML
    private TextField Cateform_note;

    @FXML
    private Button Cateform_submit;
    private Alert alert;
    PreparedStatement preparedStatement;
    Statement statement;

    database db = new database();
    Connection connection = db.getConnection();
    ResultSet result;

    public void Clear(){
        Cateform_id.setText("");
        Cateform_name.setText("");
        Cateform_note.setText("");
    }
    public void Cancel(){
        Cateform_cancel.getScene().getWindow().hide();
    }
    public static void setUpdate(){
        Cateform_label.setText("UPDATE CATEGORY");
    }
    public void submit() {
        if (Cateform_id.getText().isEmpty() ||
                Cateform_name.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String checkidcategory = "SELECT category_id FROM category WHERE category_id = '"
                    + Cateform_id.getText() + "'";
            try {
                statement = connection.createStatement();
                result = statement.executeQuery(checkidcategory);
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(Cateform_id.getText() + "is already taken");
                    alert.showAndWait();
                } else {
                    String insertCategory = "INSERT INTO category "
                            + "(category_id,category_name,note)"
                            + "VALUE(?,?,?)";
                    preparedStatement = connection.prepareStatement(insertCategory);
                    preparedStatement.setString(1,Cateform_id.getText());
                    preparedStatement.setString(2,Cateform_name.getText());
                    preparedStatement.setString(3,Cateform_note.getText());

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Add Category");
                    alert.showAndWait();
                    Cateform_submit.getScene().getWindow().hide();

                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Mainview controller = fxmlLoader.<Mainview>getController();
                    controller.CateshowData();
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Cateform_submit.setOnAction(event1 -> {
                        Event.fireEvent(stage ,new ActionEvent());
                    });

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private Category category;

    public CategoryForm() {
    }
    String idcate;
    String namecate;
    String notecate;
    String cateid;
    public void setData(Category category){
        this.category = category;
        cateid = category.getCategory_ID();
        idcate = category.getCategory_ID();
        namecate = category.getCategory_Name();
        notecate = category.getCategory_Note();

        Cateform_id.setText(idcate);
        Cateform_name.setText(namecate);
        Cateform_note.setText(notecate);
    }

    @FXML
    private Button Cateform_submit2;
    public void submitupdate() {
        if (Cateform_id.getText().isEmpty()||
                Cateform_name.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else {
            String updatedata = "UPDATE category SET category_id=?,category_name=?,note=? WHERE category_id=?";
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("R U sure U want to update category id: "+Cateform_id.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    preparedStatement = connection.prepareStatement(updatedata);
                    preparedStatement.setString(1,Cateform_id.getText());
                    preparedStatement.setString(2,Cateform_name.getText());
                    preparedStatement.setString(3,Cateform_note.getText());
                    preparedStatement.setString(4,cateid);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();
                    Cateform_submit2.getScene().getWindow().hide();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Mainview controller = fxmlLoader.<Mainview>getController();
                    controller.CateshowData();
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Cateform_submit2.setOnAction(event1 -> {
                        Event.fireEvent(stage ,new ActionEvent());
                    });
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
