module nlcs.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens nlcs.project to javafx.fxml;
    exports nlcs.project.Controller;
    opens nlcs.project.Controller to javafx.fxml;
    exports nlcs.project;
//    opens nlcs.project.View to javafx.fxml;
    opens nlcs.project.Model;
}