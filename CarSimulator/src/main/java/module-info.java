module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;


    exports car.model;
    exports car.exception;
    exports car.app;
    opens car.app to javafx.fxml;
    exports car.listener;
}