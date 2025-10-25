module com.escape {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires freetts;
    requires java.desktop;
    opens com.escape to javafx.fxml;
    exports com.escape;
}
