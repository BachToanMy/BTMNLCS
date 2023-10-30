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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageCategoryAction);
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
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
            scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageCategoryAction);
            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            stage.show();
        }
    }

    public void CateshowData() {
        ObservableList<Category> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM category";
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            Category prod;
            while (result.next()) {
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

    public void loadCategory() {
        CateshowData();
    }

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
        ObservableList<Brand> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM brand";
        try {
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            Brand prod;
            while (result.next()) {
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

    //Product
    public void ProductshowData() {
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
        stage.initOwner(scene.getWindow());

        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        scene.getWindow().addEventHandler(ActionEvent.ACTION, this.childStageProductAction);
        stage.show();
    }

    public void loadProduct() {
        ProductshowData();
    }

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
                System.out.println(id_account);
                System.out.println(prod.getProduct_ID());
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
    @FXML
    private Button product_btn;
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button brand_btn;
    @FXML
    private Button Category_btn;
    @FXML
    private Button cart_btn;

    public void setFalseVisible() {
        Product_form.setVisible(false);
        Brand_form.setVisible(false);
        Category_form.setVisible(false);
        Cart_form.setVisible(false);
        dashboard_form.setVisible(false);
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
        } else if (event.getSource() == Category_btn) {
            setFalseVisible();
            Category_form.setVisible(true);
            CateshowData();
        } else if (event.getSource() == brand_btn) {
            setFalseVisible();
            Brand_form.setVisible(true);
            BrandshowData();
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
            System.out.println(itemListData.size());
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
    public void daily(){
        System.out.println(dashboard_datepicker.getValue());
        Double revenue=0.0;
        Integer nop=0;
        Integer nor=0;
        String sql = "SELECT COUNT(*),SUM(finaltotal) FROM receipt WHERE date=?";
        String sql2 = "SELECT SUM(quantity) FROM receipt JOIN detail_cart using(cart_id)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1,sqlDate);
            result=preparedStatement.executeQuery();
            if (result.next()){
                revenue = result.getDouble("SUM(finaltotal)");
                nor = result.getInt("COUNT(*)");
            }
            preparedStatement = connection.prepareStatement(sql2);
            result = preparedStatement.executeQuery();
            if(result.next()){
                nop = result.getInt("SUM(quantity)");
            }
            dashboard_revenue.setText(formatter.format(revenue));
            dashboard_nor.setText(nor.toString());
            dashboard_nop.setText(nop.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void revenuechart(){
        int i;
        String sql = "SELECT SUM(finaltotal) FROM receipt WHERE month(date)=?";
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
    public void chart(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setFalseVisible();
        dashboard_form.setVisible(true);
        dashboard_datepicker.setValue(sqlDate.toLocalDate());
        daily();
        revenuechart();
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
