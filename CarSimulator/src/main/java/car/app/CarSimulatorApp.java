package car.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CarSimulatorApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CarSimulatorApp.class.getResource("/car/simulatorView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Car Simulator");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.setMaximized(false);
            }
        });
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}