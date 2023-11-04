package nlcs.project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import nlcs.project.Application;
import nlcs.project.Model.*;
import nlcs.project.Model.Receipt;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
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
    private TableColumn<Product, ImageView> Product_colImage;

    @FXML
    private TableColumn<Product, Double> Product_colPricein;

    @FXML
    private TableColumn<Product, Double> Product_colPriceout;

    @FXML
    private TableColumn<Product, Integer> Product_colStock;
    @FXML
    private ComboBox<String> Product_searchbox;


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
    String username;
    String id_account;
    DecimalFormat formatter = new DecimalFormat("#.##");
    private Account account = new Account();
    @FXML private Label product_total;
    @FXML private Label brand_total;
    @FXML private Label category_total;
    public void setData(Account account) {
        username = account.getUsername();
        id_account = account.getIdaccount();
        displayName();
    }

    public void displayName() {
        String user = username.substring(0, 1).toUpperCase() + username.substring(1);
        logo_label.setText(user);
    }

    EventHandler<ActionEvent> childStageBrandAction = event -> {
        BrandshowData();
    };

    EventHandler<ActionEvent> childStageProductAction = event -> {
        ProductshowData();
    };

    EventHandler<ActionEvent> childStageCategoryAction = event -> {
        CateshowData();
    };
    EventHandler<ActionEvent> childStageItemAction = event -> {
        try {
            menuDisplayCard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };


    //PRODUCTS
    //CATEGORYS
    public void addCategory() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Category_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.initOwner(scene.getWindow());
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageCategoryAction);
    }

    public void updateCategory() throws IOException {
        Category category = Cate_table.getSelectionModel().getSelectedItem();
        if (category == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose category to update!");
            alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Category_formupdate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CategoryForm controller = fxmlLoader.<CategoryForm>getController();
            controller.setData(category);

            Stage stage = new Stage();
            stage.initOwner(scene.getWindow());

            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageCategoryAction);
        }
    }

    public void CateshowData() {
        Integer total=0;
        ObservableList<Category> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM category";
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            Category prod;
            while (result.next()) {
                total+=1;
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
        category_total.setText(total.toString());
    }

    public void deleteCategory() {
        Category category = Cate_table.getSelectionModel().getSelectedItem();
        if (category == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose category to delete!");
            alert.showAndWait();
        } else {
            String id = category.getCategory_ID();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you want to Delete product id : " + id);
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deletedata = "DELETE FROM category WHERE category_id = ?";
                try {
                    preparedStatement = connection.prepareStatement(deletedata);
                    preparedStatement.setString(1, id);
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
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }

        }
    }

//    public void loadCategory() {
//        CateshowData();
//    }

    //BRAND MANAGEMENT
    public void addBrand() throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Brand_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.initOwner(scene.getWindow());


        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageBrandAction);
    }

    public void BrandshowData() {
        Integer total=0;
        ObservableList<Brand> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM brand";
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            Brand prod;
            while (result.next()) {
                total+=1;
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
        brand_total.setText(total.toString());
    }

    public void deleteBrand() {
        Brand brand = Brand_table.getSelectionModel().getSelectedItem();
        if (brand == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to delete!");
            alert.showAndWait();
        } else {
            String id = brand.getBrand_ID();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you want to Delete BRAND id : " + id);
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deletedata = "DELETE FROM brand WHERE brand_id = ?";
                try {
                    preparedStatement = connection.prepareStatement(deletedata);
                    preparedStatement.setString(1, id);
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
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }

        }
    }

    public void loadBrand() {
        BrandshowData();
    }

    public void updateBrand() throws IOException {
        Brand brand = Brand_table.getSelectionModel().getSelectedItem();
        if (brand == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to update!");
            alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Brand_formupdate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            BrandFormController controller = fxmlLoader.<BrandFormController>getController();
            controller.setData(brand);


            Stage stage = new Stage();
            stage.initOwner(scene.getWindow());


            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageBrandAction);

        }
    }

    public void ProductsearchBOX() {
        String sql = "SELECT * FROM category";
        List<String> searchList = new ArrayList<>();
        searchList.add("ALL");
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                String category = result.getString("category_id") + "-" + result.getString("category_name");
                searchList.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql2 = "SELECT * FROM brand";
        try {
            preparedStatement = connection.prepareStatement(sql2);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                String category = result.getString("brand_id") + "-" + result.getString("brand_name");
                searchList.add(category);
            }
            ObservableList Listsearch = FXCollections.observableArrayList(searchList);
            Product_searchbox.setItems(Listsearch);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //////////////////////////////////////////////////////////////////////PRODUCT///////////////////////////////////////////////////////////
    public void ProductshowData() {
        Integer total=0;
        ObservableList<Product> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        String searchbox = Product_searchbox.getSelectionModel().getSelectedItem();
        if ((searchbox != null) && (searchbox.toString() != "ALL")) {
            Integer index = Product_searchbox.getSelectionModel().getSelectedItem().toString().indexOf("-");
            String search = Product_searchbox.getSelectionModel().getSelectedItem().toString().substring(0, index);
            String checkproid = "SELECT * FROM product WHERE category_id = ? OR brand_id = ?";
            try {
                preparedStatement = connection.prepareStatement(checkproid);
                preparedStatement.setString(1, search);
                preparedStatement.setString(2, search);
                result = preparedStatement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                preparedStatement = connection.prepareStatement(sql);
                result = preparedStatement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Product prod;
            while (result.next()) {
                total = total + result.getInt("stock");
                String category = null;
                String brand = null;
                String getCategory = "SELECT category_name FROM category WHERE category_id = ?";
                preparedStatement1 = connection1.prepareStatement(getCategory);
                preparedStatement1.setString(1, result.getString("category_id"));
                result1 = preparedStatement1.executeQuery();
                if (result1.next()) {
                    category = result1.getString("category_name");
                }
                String getBrand = "SELECT brand_name FROM brand WHERE brand_id = ?";
                preparedStatement2 = connection2.prepareStatement(getBrand);
                preparedStatement2.setString(1, result.getString("brand_id"));
                result2 = preparedStatement2.executeQuery();
                if (result2.next()) {
                    brand = result2.getString("brand_name");
                }
//                String path = "File:"+result.getString("image");
//                System.out.println(path);
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
//        Product_colImage.setCellValueFactory(new PropertyValueFactory<Product,ImageView>("image"));
        Product_colNote.setCellValueFactory(new PropertyValueFactory<>("Note"));

        Product_table.setItems(listData);
        product_total.setText(total.toString());
    }
    @FXML private ImageView product_imageview;
    public void ShowProductImage(){
        Product prod = Product_table.getSelectionModel().getSelectedItem();
        String path = "File:" + prod.getImage();
        Image image = new Image(path,134,126,false,true);
        product_imageview.setImage(image);
    }
    public void addProduct() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Product_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.initOwner(scene.getWindow());

        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageProductAction);
        stage.show();
    }

//    public void loadProduct() {
//        ProductshowData();
//    }

    public void deleteProduct() {
        Product prod = Product_table.getSelectionModel().getSelectedItem();
        if (prod == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to delete!");
            alert.showAndWait();
        } else {
            String id = prod.getProduct_ID();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you want to Delete BRAND id : " + id);
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deletedata = "DELETE FROM product WHERE product_id = ?";
                String deletedetail = "DELETE FROM detail_cart WHERE product_id=?";
                try {
                    preparedStatement = connection.prepareStatement(deletedata);
                    preparedStatement.setString(1, id);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    preparedStatement=connection.prepareStatement(deletedetail);
                    preparedStatement.setString(1,id);
                    preparedStatement.executeUpdate();
                    ProductshowData();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
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
        if (product == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose brand to update!");
            alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Product_formupdate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ProductFormController controller = fxmlLoader.<ProductFormController>getController();
            controller.setData(product);

            Stage stage = new Stage();
            stage.initOwner(scene.getWindow());

            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageProductAction);
            stage.show();

        }
    }

    @FXML
    private Button Product_addCartbtn;

    public void addtoCart() {
        Product prod = Product_table.getSelectionModel().getSelectedItem();
        if (prod == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose product to add to cart!");
            alert.showAndWait();
        } else if (prod.getStock() < 1) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setContentText(null);
            alert.setContentText(prod.getProduct_ID() + "is out of stock!");
            alert.showAndWait();
        } else {
            try {
                CallableStatement statement1 = connection.prepareCall("CALL ThemSP(?,?)");
                statement1.setString(1, id_account);
                statement1.setString(2, prod.getProduct_ID());
//                System.out.println(id_account);
//                System.out.println(prod.getProduct_ID());
                result = statement1.executeQuery();
//                System.out.println(result);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information message");
                alert.setHeaderText(null);
                alert.setContentText("Add product to cart successfully!");
                alert.showAndWait();
                ProductshowData();
            } catch (SQLException e) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Add product to cart unsuccessfully!");
                alert.showAndWait();
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    private AnchorPane dashboard_form;
    @FXML
    private AnchorPane Product_form;
    @FXML
    public AnchorPane Cart_form;
    @FXML
    private AnchorPane Category_form;
    @FXML
    private AnchorPane Brand_form;
    @FXML AnchorPane Receipt_form;
    @FXML
    private Button product_btn;
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button Brand_btn;
    @FXML
    private Button Category_btn;
    @FXML private Button Receipt_btn;
    @FXML
    private Button cart_btn;

    public void setFalseVisible() {
        Product_form.setVisible(false);
        Brand_form.setVisible(false);
        Category_form.setVisible(false);
        Cart_form.setVisible(false);
        dashboard_form.setVisible(false);
        Receipt_form.setVisible(false);
    }

    public void switchForm(ActionEvent event) throws IOException {

        if (event.getSource() == product_btn) {
            setFalseVisible();
            Product_form.setVisible(true);
            ProductshowData();
            ProductsearchBOX();
        } else if (event.getSource() == dashboard_btn) {
            setFalseVisible();
            dashboard_form.setVisible(true);
            daily();
            revenuechart();
            barchart();
            db_table();
            setDashboard_piechart(new ActionEvent());
        } else if (event.getSource() == Category_btn) {
            setFalseVisible();
            Category_form.setVisible(true);
            CateshowData();
        } else if (event.getSource() == Brand_btn) {
            setFalseVisible();
            Brand_form.setVisible(true);
            BrandshowData();
        } else if (event.getSource()==Receipt_btn){
            setFalseVisible();
            Receipt_form.setVisible(true);
            rcp_comdate();
            ReceiptShowData();
        } else {
            setFalseVisible();
            Cart_form.setVisible(true);
            menuDisplayCard();
        }
    }

    //ITEMS
    @FXML
    private Label cart_idcart;
    private int cart_id;
    private int cart_status;
    private int tonghang = 0;
    private Double tongcong = 0.0;
    private Label cart_labelpaid;

    public ObservableList<Item> ItemGetData() {
        ObservableList<Item> listData = FXCollections.observableArrayList();
        String sql1 = "SELECT Cart_id,Cart_status FROM cart WHERE idaccount=? AND Cart_status=?";
        try {
            preparedStatement = connection.prepareStatement(sql1);
            preparedStatement.setString(1, id_account);
            preparedStatement.setInt(2, 0);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                cart_id = result.getInt("Cart_id");
//                cart_status=result.getInt("Cart_status");
                cart_idcart.setText(String.valueOf(cart_id));
                String sql = "SELECT * FROM detail_cart WHERE Cart_id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, cart_id);
                result = preparedStatement.executeQuery();
                Item item;
                tonghang=0;
                tongcong=0.0;
                while (result.next()) {
                    tonghang = tonghang + result.getInt("quantity");
                    tongcong = tongcong + result.getDouble("price");
                    item = new Item(result.getInt("Cart_id"), result.getString("product_id"),
                            result.getInt("quantity"),
                            result.getDouble("price"));
                    listData.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listData;
    }


    private ObservableList<Item> itemListData = FXCollections.observableArrayList();


    @FXML
    private GridPane cart_gridpane;
    @FXML
    private VBox cart_vbox;
    @FXML
    private ScrollPane cart_scrollpan;
    @FXML
    private Label Cart_nop;
    @FXML
    private Label Cart_total;
    @FXML private Label cart_label;
    java.util.Date date = new java.util.Date();
    java.sql.Date sqlDate = new  java.sql.Date(date.getTime());;
    public void menuDisplayCard() throws IOException {
        itemListData.clear();
        itemListData.addAll(ItemGetData());
        if(itemListData.size()==0){
            cart_scrollpan.setVisible(false);
            cart_label.setVisible(true);
            Cart_nop.setText(String.valueOf(0));
            Cart_total.setText(String.valueOf(0.0));
        } else{
            cart_scrollpan.setVisible(true);
            cart_label.setVisible(false);
            Cart_nop.setText(String.valueOf(tonghang));
            Cart_total.setText(String.valueOf(tongcong));
            int row = 0;
            int col = 0;
            cart_gridpane.getChildren().clear();
            cart_gridpane.getRowConstraints().clear();
            cart_gridpane.getColumnConstraints().clear();
//            System.out.println(itemListData.size());
            for (int m = 0; m < itemListData.size(); m++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Application.class.getResource("items.fxml"));
                try {
                    HBox anchorPane = fxmlLoader.load();
                    Items controller = fxmlLoader.<Items>getController();
                    controller.setItem(itemListData.get(m));
                    if (col == 2) {
                        col = 0;
                        row += 1;
                    }
                    cart_gridpane.add(anchorPane, col++, row);
                    GridPane.setMargin(anchorPane, new Insets(10));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void Pay() throws IOException {
        Receipt receipt = new Receipt(cart_id,Double.valueOf(Cart_total.getText()));
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("payment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ReceiptController controller = fxmlLoader.<ReceiptController>getController();
        controller.setData(receipt);

        Stage stage = new Stage();
        stage.initOwner(scene.getWindow());
        stage.setTitle("myClothing");
        stage.setResizable(false);
        stage.setScene(scene);
//        scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageCustomerAction);
        stage.show();
    }
    ///////////////////////////////////////////////////////////DASHBOARD//////////////////////////////////////////////////////////////
    @FXML private LineChart dashboard_revenuemonth;
    @FXML private Label dashboard_revenue;
    @FXML private Label dashboard_nor;
    @FXML private Label dashboard_nop;
    @FXML private DatePicker dashboard_datepicker;
    @FXML private Label dashboard_nopv;
    public void daily(){
//        System.out.println(dashboard_datepicker.getValue());
        LocalDate date = dashboard_datepicker.getValue();
        Double revenue=0.0;
        Integer nop=0;
        Integer nor=0;
        Integer nopv=0;
        String sql = "SELECT COUNT(*),SUM(finaltotal) FROM receipt WHERE date(date)=?";
        String sql2 = "SELECT SUM(quantity) FROM receipt JOIN detail_cart using(cart_id) WHERE date(date)=?";
        String sql3= "SELECT SUM(stock) FROM product";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(date));
            result=preparedStatement.executeQuery();
            if (result.next()){
                revenue = result.getDouble("SUM(finaltotal)");
                nor = result.getInt("COUNT(*)");
            }
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setDate(1, Date.valueOf(date));
            result = preparedStatement.executeQuery();
            if(result.next()){
                nop = result.getInt("SUM(quantity)");
            }
            preparedStatement = connection.prepareStatement(sql3);
            result = preparedStatement.executeQuery();
            if(result.next()){
                nopv = result.getInt("SUM(stock)");
            }
            dashboard_revenue.setText(formatter.format(revenue));
            dashboard_nor.setText(nor.toString());
            dashboard_nop.setText(nop.toString());
            dashboard_nopv.setText(nopv.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void revenuechart(){
        int i;
        String sql = "SELECT SUM(finaltotal) FROM receipt WHERE month(date)=?";
        dashboard_revenuemonth.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName("Revenue");
        for(i=1;i<=12;i++){
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,i);
                result = preparedStatement.executeQuery();
                if(result.next()){
                    Month month = Month.of(i);
                    series.getData().add(new XYChart.Data(month.toString(),result.getDouble("SUM(finaltotal)")));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        dashboard_revenuemonth.getData().add(series);
    }
    @FXML private BarChart dashboard_barchart;
    public void barchart(){
        int i;
        String sql2 = "SELECT SUM(quantity) FROM receipt JOIN detail_cart using(cart_id) WHERE month(date)=?";
        String sql = "SELECT COUNT(*) FROM receipt WHERE month(date)=?";
        dashboard_barchart.getData().clear();
        XYChart.Series series1 = new XYChart.Series<>();
        series1.setName("Receipt");
        XYChart.Series series2 = new XYChart.Series<>();
        series2.setName("Quantity");
        for(i=1;i<=12;i++) {
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, i);
                result = preparedStatement.executeQuery();
                if (result.next()) {
                    Month month = Month.of(i);
                    series1.getData().add(new XYChart.Data(month.toString(), result.getDouble("COUNT(*)")));
                }
                preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setInt(1, i);
                result=preparedStatement.executeQuery();
                if(result.next()){
                    Month month = Month.of(i);
                    series2.getData().add(new XYChart.Data(month.toString(), result.getDouble("SUM(quantity)")));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        dashboard_barchart.setTitle("Quantity of sold products and number of receipt by month");
        dashboard_barchart.getData().add(series1);
        dashboard_barchart.getData().add(series2);
    }
    @FXML private TableView<Product> dashboard_tableview;
    @FXML
    private TableColumn<Product, String> db_colID;

    @FXML
    private TableColumn<Product, String> db_colName;

    @FXML
    private TableColumn<Product, String > db_colQuan;
    public void db_table(){
        ObservableList<Product> listData = FXCollections.observableArrayList();
        String sql ="SELECT product_id, product_name, sum " +
                "FROM ( SELECT product_id, SUM(quantity) AS sum " +
                "FROM receipt JOIN detail_cart USING (cart_id) " +
                "GROUP BY product_id ORDER BY sum DESC LIMIT 5) AS derived " +
                "JOIN product USING (product_id)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();
            Product prod;
            while (result.next()) {
                prod = new Product(result.getString("product_id"),
                        result.getString("product_name"),
                        result.getInt("sum"));
                listData.add(prod);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db_colID.setCellValueFactory(new PropertyValueFactory<>("Product_ID"));
        db_colName.setCellValueFactory(new PropertyValueFactory<>("Product_Name"));
        db_colQuan.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        dashboard_tableview.setItems(listData);

    }
    @FXML private PieChart dashboard_piechart;
    @FXML private PieChart dashboard_brandchart;
    @FXML private Button cate_btn;
    @FXML private AnchorPane cate_chart;
    @FXML private  AnchorPane brand_chart;
    public void setDashboard_piechart(ActionEvent event){
        dashboard_brandchart.getData().clear();
        dashboard_brandchart.setTitle("Percentage of sold items by Brand");
        dashboard_brandchart.setLabelsVisible(true);
        String sql1="SELECT brand_name,SUM(quantity) " +
                "FROM receipt JOIN detail_cart using(cart_id) " +
                "JOIN product using(product_id) " +
                "JOIN brand using(brand_id) GROUP BY brand_id";
        dashboard_piechart.getData().clear();
        dashboard_piechart.setTitle("Percentage of sold items by Category");
        dashboard_piechart.setLabelsVisible(true);
        String sql2="SELECT category_name,SUM(quantity) " +
                "FROM receipt JOIN detail_cart using(cart_id) " +
                "JOIN product using(product_id) " +
                "JOIN category using(category_id) " +
                "GROUP BY category_id";
        if(event.getSource() == cate_btn || event.getSource()==null){
            try {
                preparedStatement = connection.prepareStatement(sql2);
                result = preparedStatement.executeQuery();
                while(result.next()){
                    String name = result.getString("category_name");
                    int sl = result.getInt("SUM(quantity)");
                    dashboard_piechart.getData().add(new PieChart.Data(name, sl));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            brand_chart.setVisible(false);
            cate_chart.setVisible(true);
        }else{
            try {
                preparedStatement = connection.prepareStatement(sql1);
                result = preparedStatement.executeQuery();
                while(result.next()){
                    String name = result.getString("brand_name");
                    int sl = result.getInt("SUM(quantity)");
                    dashboard_brandchart.getData().add(new PieChart.Data(name, sl));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            cate_chart.setVisible(false);
            brand_chart.setVisible(true);

        }
    }
    ////////////////////////////////////////////////////////////RECEIPT/////////////////////////////////////////
    @FXML private TableView<Receipt> Receipt_table;
    @FXML
    private TableColumn<Receipt, java.util.Date> Receipt_colDate;

    @FXML
    private TableColumn<Receipt, Integer> Receipt_colDiscount;

    @FXML
    private TableColumn<Receipt, Double> Receipt_colFinalTotal;

    @FXML
    private TableColumn<Receipt, Integer> Receipt_colID;

    @FXML
    private TableColumn<Receipt, Integer> Receipt_colName;

    @FXML
    private TableColumn<Receipt, String> Receipt_colNote;

    @FXML
    private TableColumn<Receipt, Double> Receipt_colPaid;

    @FXML
    private TableColumn<Receipt, Double> Receipt_colTotal;
    @FXML private Label receipt_total;

    @FXML private ComboBox rcp_combobox;
    @FXML private DatePicker rcp_datepicker;
    public void rcp_comdate(){
        List<String> chuoi = new ArrayList<>();
        int i;
        for(i=1;i<=12;i++){
            chuoi.add(Month.of(i).toString());
        }
        chuoi.add("ALL");
        ObservableList list = FXCollections.observableArrayList(chuoi);
        rcp_combobox.setItems(list);
        rcp_datepicker.setValue(sqlDate.toLocalDate());

    }
    public void ReceiptshowDatabyMonth() {
        Receipt_table.getItems().clear();
        ObservableList<Receipt> listData = FXCollections.observableArrayList();
        if( rcp_combobox.getSelectionModel().getSelectedItem()!=null && rcp_combobox.getSelectionModel().getSelectedItem().toString() == "ALL"){
            ReceiptShowData();
        }else {
            Month month = Month.valueOf(rcp_combobox.getSelectionModel().getSelectedItem().toString());
            String sql = "SELECT * FROM receipt WHERE month(date)=?";
            Integer tong=0;
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,month.ordinal()+1);
                result = preparedStatement.executeQuery();
                Receipt receipt;
                while (result.next()){
                    tong+=1;
                    receipt = new Receipt(result.getInt("receipt_id"),
                            result.getInt("Cart_id"),result.getDouble("total"),
                            result.getInt("discount"),result.getDouble("finaltotal"),
                            result.getDouble("paid"),result.getDate("date"),result.getString("note"));
                    listData.add(receipt);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Receipt_colID.setCellValueFactory(new PropertyValueFactory<>("receipt_id"));
            Receipt_colName.setCellValueFactory(new PropertyValueFactory<>("cart_id"));
            Receipt_colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            Receipt_colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            Receipt_colFinalTotal.setCellValueFactory(new PropertyValueFactory<>("finaltotal"));
            Receipt_colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
            Receipt_colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            Receipt_colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
            receipt_total.setText(tong.toString());
            Receipt_table.setItems(listData);
        }
    }
    public void ReceiptshowDatabyDate(ActionEvent event) {
        Receipt_table.getItems().clear();
        ObservableList<Receipt> listData = FXCollections.observableArrayList();
        LocalDate date = rcp_datepicker.getValue();
        rcp_combobox.setValue(Month.of(date.getMonthValue()).toString());
        String sql = "SELECT * FROM receipt WHERE date(date)=?";
        Integer tong = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(date));
            result = preparedStatement.executeQuery();
            Receipt receipt;
            while (result.next()) {
                tong+=1;
                receipt = new Receipt(result.getInt("receipt_id"),
                        result.getInt("Cart_id"), result.getDouble("total"),
                        result.getInt("discount"), result.getDouble("finaltotal"),
                        result.getDouble("paid"), result.getDate("date"), result.getString("note"));
                listData.add(receipt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Receipt_colID.setCellValueFactory(new PropertyValueFactory<>("receipt_id"));
        Receipt_colName.setCellValueFactory(new PropertyValueFactory<>("cart_id"));
        Receipt_colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        Receipt_colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        Receipt_colFinalTotal.setCellValueFactory(new PropertyValueFactory<>("finaltotal"));
        Receipt_colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        Receipt_colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        Receipt_colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        receipt_total.setText(tong.toString());
        Receipt_table.setItems(listData);
    }

    public void ReceiptShowData(){
        ObservableList<Receipt> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM receipt";
        Integer  tong=0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            result=preparedStatement.executeQuery();
            Receipt receipt;
            while (result.next()){
                tong+=1;
                receipt = new Receipt(result.getInt("receipt_id"),
                        result.getInt("Cart_id"),result.getDouble("total"),
                        result.getInt("discount"),result.getDouble("finaltotal"),
                        result.getDouble("paid"),result.getDate("date"),result.getString("note"));
                listData.add(receipt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Receipt_colID.setCellValueFactory(new PropertyValueFactory<>("receipt_id"));
        Receipt_colName.setCellValueFactory(new PropertyValueFactory<>("cart_id"));
        Receipt_colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        Receipt_colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        Receipt_colFinalTotal.setCellValueFactory(new PropertyValueFactory<>("finaltotal"));
        Receipt_colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        Receipt_colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        Receipt_colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        receipt_total.setText(tong.toString());
        Receipt_table.setItems(listData);
    }
    @FXML private Button logout_btn;
    public void logout(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("signup.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Welcome to myClothing");
            stage.setScene(scene);
            stage.show();
            logout_btn.getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setFalseVisible();
        dashboard_form.setVisible(true);
        dashboard_datepicker.setValue(sqlDate.toLocalDate());
        daily();
        revenuechart();
        barchart();
        db_table();
        setDashboard_piechart(new ActionEvent());
        Cart_form.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    menuDisplayCard();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println("Bạn đã nhấn Enter!");
            }
        });


//        itemListData.addListener(new ListChangeListener<Item> () {
//            @Override
//            public void onChanged (Change <? extends Item > change) {
//                while (change.next()) {
//                    if (change.wasAdded()) {
//                        System.out.println("Added: " + change.getAddedSubList());
//                    }
//                    if (change.wasRemoved()) {
//                        System.out.println("Removed: " + change.getRemoved());
//                    }
//                    if (change.wasUpdated()) {
//                        System.out.println("Updated: " + change.getList().subList(change.getFrom(), change.getTo()));
//
//                    }
//                }
//            }
//        });

//      menuDisplayCard();
//        CateshowData();
//        BrandshowData();
//        ProductsearchBOX();
//        ProductshowData();'
    }


}
