package nlcs.project.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import nlcs.project.Model.Receipt;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nlcs.project.Model.database;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ReceiptController {
    @FXML
    private TextField Cus_address;

    @FXML
    private TextField Cus_contact;

    @FXML
    private TextField Cus_name;

    @FXML
    private TextField Cus_note;

    @FXML
    private AnchorPane Product_form;

    @FXML
    private Button Proform_cancel;

    @FXML
    private Button Proform_clear;

    @FXML
    private Label Proform_label;

    @FXML
    private Label Proform_label1;

    @FXML
    private TextField Proform_note1;

    @FXML
    private TextField Proform_note11;

    @FXML
    private Button Proform_submit;

    @FXML
    private TextField cus_change;

    @FXML
    private TextField cus_discount;

    @FXML
    private TextField cus_finaltotal;

    @FXML
    private TextField cus_paid;

    @FXML
    private TextField cus_total;
    @FXML private Button Cus_Sumbit;
    @FXML private Button Cus_cancel;
    private int cart_id;
    Receipt customer;
    Double total=0.0;
    Double totally=0.0;
    Double discount=0.0;
    @FXML private TextField cus_note;
    PreparedStatement preparedStatement;
    Statement statement;

    database db = new database();
    Connection connection = db.getConnection();
    ResultSet result;
    DecimalFormat formatter = new DecimalFormat("#.##");
    private Alert alert;
    java.util.Date date = new java.util.Date();
    java.sql.Date sqlDate = new  java.sql.Date(date.getTime());
    // Lấy ngày hệ thống hiện tại
    LocalDateTime now = LocalDateTime.now();

    // Chuyển đổi ngày giờ hệ thống thành một đối tượng `java.sql.Timestamp`
    Timestamp timestamp = Timestamp.valueOf(now);
    public void Pay(ActionEvent event) {
        String sql = "INSERT INTO receipt(Cart_id,total,discount,finaltotal,paid,note,date)VALUE(?,?,?,?,?,?,?)";
        String sql2 = "UPDATE cart SET Cart_status=? WHERE Cart_id=?";
        if(cus_discount.getText()==null||cus_paid.getText()==null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR MESSAGE");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blanks");
            alert.showAndWait();
        }else {
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,cart_id);
                preparedStatement.setDouble(2,total);
                preparedStatement.setInt(3,Integer.parseInt(cus_discount.getText()));
                preparedStatement.setDouble(4,Double.valueOf(cus_finaltotal.getText()));
                preparedStatement.setDouble(5, Double.parseDouble(formatter.format(Double.valueOf(cus_paid.getText()))));
                preparedStatement.setString(6,cus_note.getText());
                preparedStatement.setTimestamp(7,timestamp);
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setInt(1,1);
                preparedStatement.setInt(2,cart_id);
                preparedStatement.executeUpdate();

                HashMap map = new HashMap();
                map.put("getInvoice",cart_id);
                map.put("getIdaccount",1);
                map.put("getIdreceipt",1);
                map.put("getTotal",total);
                Integer discount1 = Integer.parseInt(cus_discount.getText());
                map.put("getDiscount",discount1);
                Double totally1 = Double.valueOf(cus_finaltotal.getText());
                map.put("getFinaltotal",totally1);
                Double paid = Double.parseDouble(formatter.format(Double.valueOf(cus_paid.getText())));
                map.put("getPaid",paid);
                Double change = Double.parseDouble(formatter.format(Double.valueOf(cus_change.getText())));
                map.put("getChange",change);

                JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\ASUS\\JaspersoftWorkspace\\Receipt\\myInvoice.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,map,connection);

                JasperViewer.viewReport(jasperPrint,false);


                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION MESSAGE");
                alert.setHeaderText(null);
                alert.setContentText("Successful Payment!");
                alert.showAndWait();

                Cus_Sumbit.getScene().getWindow().hide();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void Cancel(ActionEvent event) {
        Cus_cancel.getScene().getWindow().hide();
    }


    public void setData(Receipt receipt) {
        cart_id=receipt.getCart_id();
        total = receipt.getTotal();
        cus_total.setText(receipt.getTotal().toString());
    }


    public void discount(ActionEvent event){
        discount = Double.valueOf(cus_discount.getText())/100;
        totally  = total-total*discount;
        cus_finaltotal.setText(String.valueOf(totally));
    }

    public void change(){
        Double totl = Double.valueOf(cus_finaltotal.getText());
        Double paidd = Double.valueOf(cus_paid.getText());
        String roundedNumber = formatter.format(paidd - totl);
        cus_change.setText(String.valueOf(roundedNumber));
    }
}
