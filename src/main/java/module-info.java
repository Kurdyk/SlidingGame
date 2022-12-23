module com.taquin.taquin {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.Application to javafx.fxml;
    exports com.Application;
}