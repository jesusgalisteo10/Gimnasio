module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;

    opens com.example to javafx.fxml;
    opens com.example.model.connection to java.xml.bind;

    exports com.example;
    exports com.example.utils;
    opens com.example.utils to javafx.fxml;
    exports com.example.view;
    opens com.example.view to javafx.fxml;

}
