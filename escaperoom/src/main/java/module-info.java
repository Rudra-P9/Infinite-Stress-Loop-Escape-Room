module com.escape {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.escape to javafx.fxml;
    exports com.escape;
}
