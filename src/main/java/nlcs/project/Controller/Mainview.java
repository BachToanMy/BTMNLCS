package nlcs.project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nlcs.project.Application;
import nlcs.project.Model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class Mainview implements Initializable {
    @FXML
    private Label logo_label;
    private Alert alert;
    @FXML
    private TableColumn<Category, String> Cate_colID;
    @FXML
    private TableView<Category> Cate_table;

    @FXML
    private TableColumn<Category, String> Cate_colName;

    @FXML
    private TableColumn<Category, String> Cate_colNote;
    //Brand
    @FXML
    private Button Brand_Add;

    @FXML
    private Button Brand_Delete;

    @FXML
    private Button Brand_Update;

    @FXML
    private TableColumn<Brand, String> Brand_colID;

    @FXML
    private TableColumn<Brand, String> Brand_colName;

    @FXML
    private TableColumn<Brand, String> Brand_colNote;


    @FXML
    private Button Brand_load;

    @FXML
    private TableView<Brand> Brand_table;
    @FXML
    private TableColumn<Product, String> Product_colBrand;

    @FXML
    private TableColumn<Product, String> Product_colCategory;

    @FXML
    private TableColumn<Product, Date> Product_colDate;

    @FXML
    private TableColumn<Product, String> Product_colID;

    @FXML
    private TableColumn<Product, String> Product_colName;

    @FXML
    private TableColumn<Product, String> Product_colNote;

    @FXML
    private TableColumn<Product, Double> Product_colPricein;

    @FXML
    private TableColumn<Product, Double> Product_colPriceout;

    @FXML
    private TableColumn<Product, Integer> Product_colStock;

    @FXML
    private AnchorPane Product_form;

    @FXML
    private TableView<Product> Product_table;
    PreparedStatement preparedStatement;
    Statement statement;

    database db = new database();
    Connection connection = db.getConnection();
    ResultSet result;
    PreparedStatement preparedStatement1;

    database db1 = new database();
    Connection connection1 = db1.getConnection();
    ResultSet result1;
    PreparedStatement preparedStatement2;

    database db2 = new database();
    Connection connection2 = db2.getConnection();
    ResultSet result2;
    private Account account = new Account();
    public void displayName(){
        String user = Account.username;
        user = user.substring(0,1).toUpperCase() + user.substring(1);
        logo_label.setText(user);
    }
    //PRODUCTS
    //CATEGORYS
    public void addCategory() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Category_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public void updateCategory() throws IOException {
        Category category = Cate_table.getSelectionModel().getSelectedItem();
        if(category==null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose category to update!");
            alert.showAndWait();
        }else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Category_formupdate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CategoryForm controller = fxmlLoader.<CategoryForm>getController();
            controller.setData(category);

            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            stage.show();
        }
    }

    public void CateshowData(){
        ObservableList<Category> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM category";
        try{
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            Category prod;
            while(result.next()){
                prod = new Category(result.getString("category_id"),
                        result.getString("category_name"),
                        result.getString("note"));
                listData.add(prod);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cate_colID.setCellValueFactory(new PropertyValueFactory<>("Category_ID"));
        Cate_colName.setCellValueFactory(new PropertyValueFactory<>("Category_Name"));
        Cate_colNote.setCellValueFactory(new PropertyValueFactory<>("Category_Note"));

        Cate_table.setItems(listData);
    }

    public void deleteCategory(){
        Category category = Cate_table.getSelectionModel().getSelectedItem();
        if (category == null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose category to delete!");
            alert.showAndWait();
        }else {
            String id = category.getCategory_ID();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you want to Delete product id : "+id);
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                String deletedata = "DELETE FROM category WHERE category_id = ?";
                try{
                    preparedStatement = connection.prepareStatement(deletedata);
                    preparedStatement.setString(1,id);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    CateshowData();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }

        }
    }

    public void loadCategory(){
        CateshowData();
    }
    //BRAND MANAGEMENT
    public void addBrand() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Brand_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public void BrandshowData(){
        ObservableList<Brand> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM brand";
        try{
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            Brand prod;
            while(result.next()){
                prod = new Brand(result.getString("brand_id"),
                        result.getString("brand_name"),
                        result.getString("note"));
                listData.add(prod);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Brand_colID.setCellValueFactory(new PropertyValueFactory<>("Brand_ID"));
        Brand_colName.setCellValueFactory(new PropertyValueFactory<>("Brand_Name"));
        Brand_colNote.setCellValueFactory(new PropertyValueFactory<>("Brand_Note"));

        Brand_table.setItems(listData);
    }
    public void deleteBrand(){
        Brand brand = Brand_table.getSelectionModel().getSelectedItem();
        if (brand == null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to delete!");
            alert.showAndWait();
        }else {
            String id = brand.getBrand_ID();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you want to Delete BRAND id : "+id);
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                String deletedata = "DELETE FROM brand WHERE brand_id = ?";
                try{
                    preparedStatement = connection.prepareStatement(deletedata);
                    preparedStatement.setString(1,id);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    BrandshowData();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }

        }
    }
    public void loadBrand(){
        BrandshowData();
    }
    public void updateBrand() throws IOException {
        Brand brand = Brand_table.getSelectionModel().getSelectedItem();
        if(brand==null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to update!");
            alert.showAndWait();
        }else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Brand_formupdate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            BrandFormController controller = fxmlLoader.<BrandFormController>getController();
            controller.setData(brand);

            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            stage.show();
        }
    }
    //Product
    public void ProductshowData(){
        ObservableList<Product> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        try{
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();
            Product prod;
            while(result.next()){
                String category = null;
                String brand=null;
                String getCategory = "SELECT category_name FROM category WHERE category_id = ?";
                preparedStatement1 = connection1.prepareStatement(getCategory);
                preparedStatement1.setString(1,result.getString("category_id"));
                result1=preparedStatement1.executeQuery();
                if(result1.next()){
                    category = result1.getString("category_name");
                }
                String getBrand = "SELECT brand_name FROM brand WHERE brand_id = ?";
                preparedStatement2 = connection2.prepareStatement(getBrand);
                preparedStatement2.setString(1,result.getString("brand_id"));
                result2=preparedStatement2.executeQuery();
                if(result2.next()){
                    brand = result2.getString("brand_name");
                }
                prod = new Product(result.getString("product_id"),
                                    result.getString("product_name"),
                                    result.getString("category_id"),
                                    category,
                                    result.getString("brand_id"),
                                    brand,
                                    result.getDouble("price_in"),
                                    result.getDouble("price_out"),
                                    result.getInt("stock"),
                                    result.getString("image"),
                                    result.getDate("date"),
                                    result.getString("note"));
                listData.add(prod);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Product_colID.setCellValueFactory(new PropertyValueFactory<>("Product_ID"));
        Product_colName.setCellValueFactory(new PropertyValueFactory<>("Product_Name"));
        Product_colCategory.setCellValueFactory(new PropertyValueFactory<>("Category_name"));
        Product_colBrand.setCellValueFactory(new PropertyValueFactory<>("Brand_name"));
        Product_colPricein.setCellValueFactory(new PropertyValueFactory<>("Price_in"));
        Product_colPriceout.setCellValueFactory(new PropertyValueFactory<>("Price_out"));
        Product_colStock.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        Product_colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        Product_colNote.setCellValueFactory(new PropertyValueFactory<>("Note"));

        Product_table.setItems(listData);
    }
    public void addProduct() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Product_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public void loadProduct(){
        ProductshowData();
    }
    public void deleteProduct(){
        Product prod = Product_table.getSelectionModel().getSelectedItem();
        if (prod == null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to delete!");
            alert.showAndWait();
        }else {
            String id = prod.getProduct_ID();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you want to Delete BRAND id : "+id);
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                String deletedata = "DELETE FROM product WHERE product_id = ?";
                try{
                    preparedStatement = connection.prepareStatement(deletedata);
                    preparedStatement.setString(1,id);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    ProductshowData();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }

        }
    }
    public void updateProduct() throws IOException {
        Product product = Product_table.getSelectionModel().getSelectedItem();
        if(product==null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to update!");
            alert.showAndWait();
        }else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Product_formupdate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ProductFormController controller = fxmlLoader.<ProductFormController>getController();
            controller.setData(product);

            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            stage.show();
        }
    }
    public void addtoCart(){}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//      displayName();
//        CateshowData();
//        BrandshowData();
        ProductshowData();

    }
}
