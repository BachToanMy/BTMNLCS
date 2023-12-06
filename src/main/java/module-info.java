module nlcs.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.codec;
    requires jasperreports;
    requires org.slf4j;
    requires com.rabbitmq.client;


    opens nlcs.project to javafx.fxml;
    exports nlcs.project.Controller;
    exports nlcs.project;
//    opens nlcs.project.View to javafx.fxml;
    opens nlcs.project.Model;
    opens nlcs.project.Controller;
}