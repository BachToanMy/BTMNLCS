package nlcs.project.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nlcs.project.Model.Item;
import nlcs.project.Model.database;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class detail_cart {

        @FXML
        private AnchorPane anchor_image;

        @FXML
        private AnchorPane anchor_name;

        @FXML
        private AnchorPane anchor_price;

        @FXML
        private ImageView item_delete;

        @FXML
        private Button item_descrease;

        @FXML
        private Label item_dongia;

        @FXML
        private ImageView item_image;

        @FXML
        public Button item_increase;

        @FXML
        private Label item_name;

        @FXML
        private Label item_price;

        @FXML
        private TextField item_quantity;
        private Item item;
        @FXML
        private Label item_total;

        @FXML
        private HBox item_hbox;
        @FXML
        private Label item_prodid;

        PreparedStatement preparedStatement;
        database db = new database();
        Connection connection = db.getConnection();
        ResultSet result;
        String prodid;
        Alert alert;
        int cartid;
        double price_out;
        int stock;


        public void setItem(Item item){
                this.item = item;
                item_prodid.setText(item.getItem_proid());
                item_quantity.setText(item.getItem_quantity().toString());
                item_total.setText(item.getItem_price().toString());
                prodid= item.getItem_proid();
                cartid = item.getItem_cartid();
                String sql="SELECT product_name,price_out,image,stock FROM " +
                        "product WHERE product_id = ?";
                try{
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1,prodid);
                        result = preparedStatement.executeQuery();
                        if(result.next()){
                                price_out = result.getDouble("price_out");
                                stock = result.getInt("stock");
                                item_name.setText(result.getString("product_name"));
                                item_price.setText(result.getString("price_out"));
                                String path = "File:"+result.getString("image");
                                Image image = new Image(path,126,124,false,true);
                                item_image.setImage(image);
                        }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

        }
        int checkstk = 0;
        public void increaseQuantity(ActionEvent event) {
                String checkStock = "SELECT price_out,stock FROM product WHERE product_id = ?";
                try {
                        preparedStatement = connection.prepareStatement(checkStock);
                        preparedStatement.setString(1,prodid);
                        result = preparedStatement.executeQuery();
                        if(result.next()){
                                checkstk = result.getInt("stock");
                                if(checkstk<1){
                                        alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Error Message");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Product is out of stock");
                                        alert.showAndWait();
                                }else{
                                        int sl = Integer.parseInt(item_quantity.getText());
                                        String sql = "UPDATE detail_cart SET quantity=?,price=? WHERE product_id = ? AND Cart_id=?";
                                        preparedStatement= connection.prepareStatement(sql);
                                        preparedStatement.setInt(1,sl+1);
                                        preparedStatement.setDouble(2,(sl+1)*price_out);
                                        preparedStatement.setString(3,prodid);
                                        preparedStatement.setInt(4,cartid);
                                        preparedStatement.executeUpdate();
                                        String sql2 = "UPDATE product SET stock=? WHERE product_id=?";
                                        preparedStatement=connection.prepareStatement(sql2);
                                        preparedStatement.setInt(1,checkstk-1);
                                        preparedStatement.setString(2,prodid);
                                        preparedStatement.executeUpdate();
//                                        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainview.fxml"));
//                                        Mainview mainview = new Mainview();
//                                        mainview.Cart_form = fxmlLoader.load();
//                                        Mainview controller = fxmlLoader.<Mainview>getController();
//                                        mainview.menuDisplayCard();
//                                        stage.setScene(scene);
//                                        item_increase.setOnAction(event1 -> {
//                                                Event.fireEvent(stage ,new ActionEvent());
//                                        });

                                }
                        }
                } catch (SQLException e) {
                        throw new RuntimeException(e);

                }
//                catch (IOException e) {
//                    throw new RuntimeException(e);
//                }

        }

        public void decreaseQuantity(ActionEvent event) {
                int checkstk = 0;
                if(Integer.parseInt(item_quantity.getText())==0){
                        Cancel1();
                }else{
                        int sl = Integer.parseInt(item_quantity.getText());
                        String sql = "UPDATE detail_cart SET quantity=?,price=? WHERE product_id = ? AND Cart_id=?";
                        try {
                                //UPDTAE IN detail_cart
                                preparedStatement= connection.prepareStatement(sql);
                                preparedStatement.setInt(1,sl-1);
                                preparedStatement.setDouble(2,(sl-1)*price_out);
                                preparedStatement.setString(3,prodid);
                                preparedStatement.setInt(4,cartid);
                                preparedStatement.executeUpdate();
                                //UPDATE IN product
                                String sql2 = "UPDATE product SET stock=? WHERE product_id=?";
                                preparedStatement=connection.prepareStatement(sql2);
                                preparedStatement.setInt(1,stock+1);
                                preparedStatement.setString(2,prodid);
                                preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                                throw new RuntimeException(e);
                        }



                }
        }

        public void Cancel(MouseEvent mouseEvent) {
                String sql = "DELETE FROM detail_cart WHERE product_id=? AND Cart_id=? ";
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you want to delete product : "+prodid);
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(ButtonType.OK)) {
                        try {
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setString(1,prodid);
                                preparedStatement.setInt(2,cartid);
                                preparedStatement.executeUpdate();

                                int stk=0;
                                String checkStock = "SELECT stock FROM product WHERE product_id = ?";
                                preparedStatement = connection.prepareStatement(checkStock);
                                preparedStatement.setString(1,prodid);
                                result = preparedStatement.executeQuery();
                                if(result.next()){
                                        stk=result.getInt("stock");
                                }
                                int sl = stk+Integer.parseInt(item_quantity.getText());
                                String sql2 = "UPDATE product SET stock = ? WHERE product_id=?";
                                preparedStatement=connection.prepareStatement(sql2);
                                preparedStatement.setInt(1,sl);
                                preparedStatement.setString(2,prodid);
                                preparedStatement.executeUpdate();

                                alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("INFORMATION MESSAGE");
                                alert.setHeaderText(null);
                                alert.setContentText("Delete Successfully");
                                alert.showAndWait();
                        } catch (SQLException e) {
                                throw new RuntimeException(e);
                        }
                }


        }
        public void Cancel1() {
                String sql = "DELETE FROM detail_cart WHERE product_id=? AND Cart_id=? ";
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you want to delete product : "+prodid);
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(ButtonType.OK)) {
                        try {
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setString(1,prodid);
                                preparedStatement.setInt(2,cartid);
                                preparedStatement.executeUpdate();
                                alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("INFORMATION MESSAGE");
                                alert.setHeaderText(null);
                                alert.setContentText("Delete Successfully");
                                alert.showAndWait();
                        } catch (SQLException e) {
                                throw new RuntimeException(e);
                        }
                }

        }

        public void setQuantity() {
                int soluong;
                if(item_quantity.getText().isEmpty()){
                        soluong=0;
                }else{
                        soluong = Integer.parseInt(item_quantity.getText());

                }
                int quantity = 0;
                String getquantity = "SELECT * FROM detail_cart WHERE product_id=? AND Cart_id=?";
                String checkStock = "SELECT price_out,stock FROM product WHERE product_id = ?";
                try{
                        preparedStatement = connection.prepareStatement(getquantity);
                        preparedStatement.setString(1,prodid);
                        preparedStatement.setInt(2,cartid);
                        result = preparedStatement.executeQuery();
                        if(result.next()){
                                quantity = result.getInt("quantity");
                        }
                        preparedStatement = connection.prepareStatement(checkStock);
                        preparedStatement.setString(1, prodid);
                        result = preparedStatement.executeQuery();
                        if (result.next()) {
                                checkstk = result.getInt("stock");
                                if (checkstk < soluong-quantity) {
                                        alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Error Message");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Product is out of stock");
                                        alert.showAndWait();
                                } else {
                                        String sql = "UPDATE detail_cart SET quantity=?,price=? WHERE product_id = ? AND Cart_id=?";
                                        preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setInt(1, soluong);
                                        preparedStatement.setDouble(2, soluong * price_out);
                                        preparedStatement.setString(3, prodid);
                                        preparedStatement.setInt(4, cartid);
                                        preparedStatement.executeUpdate();
                                        String sql2 = "UPDATE product SET stock=? WHERE product_id=?";
                                        preparedStatement = connection.prepareStatement(sql2);
                                        preparedStatement.setInt(1, checkstk - (soluong-quantity));
                                        preparedStatement.setString(2, prodid);
                                        preparedStatement.executeUpdate();

                                }
                        }
                } catch (SQLException e) {
                        throw new RuntimeException(e);

                }
        }
}


