module com.escape {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires freetts;
    requires junit;
    requires java.desktop;
    opens com.escape to javafx.graphics, javafx.fxml;
    exports com.escape.model;
}
