package nlcs.project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nlcs.project.Model.Brand;
import nlcs.project.Model.Category;
import nlcs.project.Model.database;

import java.sql.*;
import java.util.Optional;

public class BrandFormController {
    @FXML
    private Button Brandform_cancel;

    @FXML
    private Button Brandform_clear;

    @FXML
    private TextField Brandform_id;

    @FXML
    private TextField Brandform_name;

    @FXML
    private TextField Brandform_note;

    @FXML
    private Button Brandform_submit;

    @FXML
    private Label Cateform_label;
    Alert alert;
    PreparedStatement preparedStatement;
    Statement statement;

    database db = new database();
    Connection connection = db.getConnection();
    ResultSet result;


    @FXML
    void Cancel(ActionEvent event) {
        Brandform_cancel.getScene().getWindow().hide();

    }

    @FXML
    void Clear(ActionEvent event) {
        Brandform_id.setText("");
        Brandform_name.setText("");
        Brandform_note.setText("");
    }

    @FXML
    void submit(ActionEvent event) {
        if (Brandform_id.getText().isEmpty() ||
                Brandform_name.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String checkidcategory = "SELECT brand_id FROM brand WHERE brand_id = ?";
            try {
                preparedStatement = connection.prepareStatement(checkidcategory);
                preparedStatement.setString(1,Brandform_id.getText());
                result = preparedStatement.executeQuery();
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(Brandform_id.getText() + "is already taken");
                    alert.showAndWait();
                } else {
                    String insertCategory = "INSERT INTO brand "
                            + "(brand_id,brand_name,note)"
                            + "VALUE(?,?,?)";
                    preparedStatement = connection.prepareStatement(insertCategory);
                    preparedStatement.setString(1,Brandform_id.getText());
                    preparedStatement.setString(2,Brandform_name.getText());
                    preparedStatement.setString(3,Brandform_note.getText());

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Add Category");
                    alert.showAndWait();
                    Brandform_submit.getScene().getWindow().hide();

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private String brandid;
    public void setData(Brand brand) {
        this.brand = brand;
        brandid = brand.getBrand_ID();
        idbrand = brand.getBrand_ID();
        namebrand = brand.getBrand_Name();
        notebrand = brand.getBrand_Note();
        Brandform_id.setText(idbrand);
        Brandform_name.setText(namebrand);
        Brandform_note.setText(notebrand);
    }
    @FXML
    private Button Brandform_submit2;
    public void submitupdate() {
        if (Brandform_id.getText().isEmpty()||
                 Brandform_name.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else {
            String updatedata = "UPDATE brand SET brand_id=?,brand_name=?,note=? WHERE brand_id=?";
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("R U sure U want to update brand id: "+Brandform_id.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    preparedStatement = connection.prepareStatement(updatedata);
                    preparedStatement.setString(1,Brandform_id.getText());
                    preparedStatement.setString(2,Brandform_name.getText());
                    preparedStatement.setString(3,Brandform_note.getText());
                    preparedStatement.setString(4,brandid);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();
                    Brandform_submit2.getScene().getWindow().hide();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private Brand brand;
    String idbrand;
    String namebrand;
    String notebrand;
}
