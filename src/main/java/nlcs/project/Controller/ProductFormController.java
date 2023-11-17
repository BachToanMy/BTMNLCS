package nlcs.project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nlcs.project.Application;
import nlcs.project.Model.Brand;
import nlcs.project.Model.Product;
import nlcs.project.Model.database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ProductFormController implements Initializable {
    @FXML
    private AnchorPane Product_form;
    @FXML
    private ComboBox<String> Proform_brand;

    @FXML
    private Button Proform_cancel;

    @FXML
    private ComboBox<String> Proform_category;

    @FXML
    private Button Proform_clear;

    @FXML
    private TextField Proform_id;

    @FXML
    private Label Proform_label;

    @FXML
    private TextField Proform_name;

    @FXML
    private TextField Proform_note;

    @FXML
    private TextField Proform_pricein;

    @FXML
    private TextField Proform_priceout;

    @FXML
    private TextField Proform_stock;

    @FXML
    private Button Proform_submit;
    @FXML
    private Button Proform_upload;
    @FXML
    private ImageView Proform_image;
    Alert alert;
    PreparedStatement preparedStatement;
    Statement statement;

    database db = new database();
    Connection connection = db.getConnection();
    ResultSet result;
    java.util.Date date = new java.util.Date();
    java.sql.Date sqlDate = new  java.sql.Date(date.getTime());;
    public void addcategoryList(){
        String sql = "SELECT * FROM category";
        List<String> categoryList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();
            while (result.next()){
                String category = result.getString("category_id")+"-"+result.getString("category_name");
                categoryList.add(category);
            }
            ObservableList Listcategory = FXCollections.observableArrayList(categoryList);
            Proform_category.setItems(Listcategory);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addbrandList(){
        String sql = "SELECT * FROM brand";
        List<String> brandList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();
            while (result.next()){
                String category = result.getString("brand_id")+"-"+result.getString("brand_name");
                brandList.add(category);
            }
            ObservableList Listcategory = FXCollections.observableArrayList(brandList);
            Proform_brand.setItems(Listcategory);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String imagepath;
    public void Upload_image(){
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("open image file","*png","*jpg"));

        File file = openFile.showOpenDialog(Product_form.getScene().getWindow());

        if(file != null){
            imagepath = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString(), 200, 214, false, true);
            Proform_image.setImage(image);
        }
    }
    public void Submit(ActionEvent event) {
        if(Proform_id.getText().isEmpty()||
            Proform_name.getText().isEmpty()||
            Proform_brand.getSelectionModel().getSelectedItem()==null||
            Proform_category.getSelectionModel().getSelectedItem() == null ||
            Proform_pricein.getText().isEmpty()||
            Proform_priceout.getText().isEmpty()||
            Proform_stock.getText().isEmpty()||
            imagepath==null
        ) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else{
            String checkproid="SELECT product_id FROM product WHERE brand_id = ?";
            try{
                preparedStatement = connection.prepareStatement(checkproid);
                preparedStatement.setString(1,Proform_id.getText());
                result = preparedStatement.executeQuery();
                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(Proform_id.getText() + "is already taken");
                    alert.showAndWait();
                }else{
                    String insertCategory = "INSERT INTO product "
                            + "(product_id,product_name,category_id,brand_id,price_in,price_out,stock,image,date,note)"
                            + "VALUE(?,?,?,?,?,?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insertCategory);
                    preparedStatement.setString(1,Proform_id.getText());
                    preparedStatement.setString(2,Proform_name.getText());
                    Integer index1 = Proform_category.getSelectionModel().getSelectedItem().toString().indexOf("-");
                    String category = Proform_category.getSelectionModel().getSelectedItem().toString().substring(0,index1);
                    preparedStatement.setString(3,category);
                    Integer index2 = Proform_brand.getSelectionModel().getSelectedItem().toString().indexOf("-");
                    String brand = Proform_brand.getSelectionModel().getSelectedItem().toString().substring(0,index2);
                    preparedStatement.setString(4,brand);
                    preparedStatement.setString(5,Proform_pricein.getText());
                    preparedStatement.setString(6,Proform_priceout.getText());
                    preparedStatement.setString(7,Proform_stock.getText());
                    imagepath = imagepath.replace("\\\\","\\");
                    preparedStatement.setString(8,imagepath);
                    preparedStatement.setString(9,String.valueOf(sqlDate));
                    preparedStatement.setString(10,Proform_note.getText());

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Add Product");
                    alert.showAndWait();
                    Proform_submit.getScene().getWindow().hide();
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Mainview controller = fxmlLoader.<Mainview>getController();
                    controller.ProductshowData();
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Proform_submit.setOnAction(event1 -> {
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

    public void Cancel(ActionEvent event) {
        Proform_cancel.getScene().getWindow().hide();
    }

    public void Clear(ActionEvent event) {
        Proform_id.setText("");
        Proform_name.setText("");
        Proform_category.getSelectionModel().clearSelection();
        Proform_brand.getSelectionModel().clearSelection();
        Proform_pricein.setText("");
        Proform_priceout.setText("");
        Proform_stock.setText("");
        Proform_note.setText("");
    }
    private String prodid;
    public void setData(Product product) {
        prodid = product.getProduct_ID();
        Proform_id.setText(product.getProduct_ID());
        Proform_name.setText(product.getProduct_Name());
        Proform_pricein.setText(product.getPrice_in().toString());
        Proform_priceout.setText(product.getPrice_out().toString());
        Proform_stock.setText(product.getStock().toString());
        Proform_note.setText(product.getNote());
        imagepath=product.getImage();
        String path = "File:" + product.getImage();
        Image image = new Image(path,200,214,false,true);
        Proform_image.setImage(image);
    }
    @FXML
    private Button Proform_submit2;
    public void Submitupdate(){
        if(Proform_id.getText().isEmpty()||
                Proform_name.getText().isEmpty()||
                Proform_brand.getSelectionModel().getSelectedItem()==null||
                Proform_category.getSelectionModel().getSelectedItem() == null ||
                Proform_pricein.getText().isEmpty()||
                Proform_priceout.getText().isEmpty()||
                Proform_stock.getText().isEmpty()||
                imagepath==null
        ){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else if(Proform_id.getText()!=prodid) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Can not change product id");
            alert.showAndWait();
        } else {
            String updatedata = "UPDATE product SET product_name=?" +
                    ",category_id=?" +
                    ",brand_id=?" +
                    ",price_in=?" +
                    ",price_out=?" +
                    ",stock=?" +
                    ",image=?" +
                    ",date=?" +
                    ",note=?" +
                    " WHERE product_id=?";
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you want to update brand id: "+Proform_id.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    preparedStatement = connection.prepareStatement(updatedata);
                    preparedStatement.setString(1,Proform_name.getText());
                    Integer index1 = Proform_category.getSelectionModel().getSelectedItem().toString().indexOf("-");
                    String category = Proform_category.getSelectionModel().getSelectedItem().toString().substring(0,index1);
                    preparedStatement.setString(2,category);
                    Integer index2 = Proform_brand.getSelectionModel().getSelectedItem().toString().indexOf("-");
                    String brand = Proform_brand.getSelectionModel().getSelectedItem().toString().substring(0,index2);
                    preparedStatement.setString(3,brand);
                    preparedStatement.setString(4,Proform_pricein.getText());
                    preparedStatement.setString(5,Proform_priceout.getText());
                    preparedStatement.setString(6,Proform_stock.getText());
                    preparedStatement.setString(7,imagepath);
                    preparedStatement.setString(8,sqlDate.toString());
                    preparedStatement.setString(9,Proform_note.getText());
                    preparedStatement.setString(10,Proform_id.getText());

                    preparedStatement.executeUpdate();


                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();
                    Proform_submit2.getScene().getWindow().hide();
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Mainview controller = fxmlLoader.<Mainview>getController();
                    controller.ProductshowData();
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Proform_submit2.setOnAction(event1 -> {
                        Event.fireEvent(stage ,new ActionEvent());
                    });
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Mainview controller = fxmlLoader.<Mainview>getController();
                    controller.ProductshowData();
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Proform_submit2.setOnAction(event1 -> {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addcategoryList();
        addbrandList();
    }

}
