package car.app;

import car.exception.CarException;
import car.exception.ClutchException;
import car.exception.EngineException;
import car.exception.GearBoxException;
import car.model.Car;
import car.model.Position;
import car.listener.Listener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Objects;

public class CarSimulatorController implements Listener {
    @FXML public Label brandLabel;
    @FXML public Label modelLabel;
    @FXML public Label licensePlateLabel;
    @FXML public Label positionXLabel;
    @FXML public Label positionYLabel;
    @FXML public Button addCarButton;
    @FXML public Button removeCarButton;

    @FXML public ToggleGroup carStateGroup;
    @FXML public ToggleButton startCarButton;
    @FXML public ToggleButton stopCarButton;

    @FXML public Button shiftDownButton;
    @FXML public Button shiftUpButton;
    @FXML public Label currentGearLabel;
    @FXML public Label gearBoxNameLabel;
    @FXML public Label gearBoxPriceLabel;
    @FXML public Label gearBoxWeightLabel;
    @FXML public Label gearCountLabel;

    @FXML public ToggleGroup clutchStateGroup;
    @FXML public ToggleButton releaseClutchButton;
    @FXML public ToggleButton pressClutchButton;
    @FXML public Label clutchNameLabel;
    @FXML public Label clutchWeightLabel;
    @FXML public Label clutchPriceLabel;

    @FXML public Button increaseRpmButton;
    @FXML public Button decreaseRpmButton;
    @FXML public Label currentRpmLabel;
    @FXML public Label maxRpmLabel;
    @FXML public Label engineStateLabel;
    @FXML public Label engineNameLabel;
    @FXML public Label enginePriceLabel;
    @FXML public Label engineWeightLabel;

    @FXML public ImageView carImageView;
    @FXML public Pane map;
    @FXML public Label carWeightLabel;

    @FXML private ComboBox<Car> selectCarComboBox;
    private ObservableList<Car> cars = FXCollections.observableArrayList();

    static Car car;

    public void addCarToList(String clutchName, int clutchWeight, int clutchPrice, int gearCount, int gearBoxPrice, int gearBoxWeight, String gearBoxName, int maxRpm, String engineName, int engineWeight, int enginePrice, String licensePlate, String model, String brand, int weight, int x, int y, int maxSpeed) {
        var newCar = createCar(clutchName, clutchWeight, clutchPrice, gearCount, gearBoxPrice, gearBoxWeight, gearBoxName, maxRpm, engineName, engineWeight, enginePrice, licensePlate, model, brand, weight, x, y, maxSpeed);
        cars.add(newCar);

        newCar.setController(this);
        currentGearLabel.setText(String.valueOf(newCar.getCurrentGear()));
        gearBoxNameLabel.setText(String.valueOf(newCar.getGearBox().getName()));
        gearBoxPriceLabel.setText(String.valueOf(newCar.getGearBox().getPrice()));
        gearBoxWeightLabel.setText(String.valueOf(newCar.getGearBox().getGearBoxTotalWeight()));
        gearCountLabel.setText(String.valueOf(gearCount));
        clutchNameLabel.setText(String.valueOf(clutchName));
        clutchPriceLabel.setText(String.valueOf(clutchPrice));
        clutchWeightLabel.setText(String.valueOf(clutchWeight));
        engineNameLabel.setText(String.valueOf(engineName));
        enginePriceLabel.setText(String.valueOf(enginePrice));
        engineWeightLabel.setText(String.valueOf(engineWeight));
        currentRpmLabel.setText(String.valueOf(newCar.getCurrentRpm()));
        maxRpmLabel.setText(String.valueOf(newCar.getMaxRpm()));
        engineStateLabel.setText(getEngineStateText());
        selectCarComboBox.getSelectionModel().select(newCar);
        carWeightLabel.setText(String.valueOf(newCar.getTotalWeight()));
    }

    public Car createCar(String clutchName, int clutchWeight, int clutchPrice, int gearCount, int gearBoxPrice, int gearBoxWeight, String gearBoxName, int maxRpm, String engineName, int engineWeight, int enginePrice, String licensePlate, String model, String brand, double weight, int x, int y, int maxSpeed) {
        car = new Car(gearCount, maxRpm, licensePlate, brand, model, maxSpeed, weight, engineName, gearBoxName, clutchName, engineWeight, gearBoxWeight, clutchWeight, enginePrice, gearBoxPrice, clutchPrice);
        car.setController(this);
        carImageView.setVisible(true);
        brandLabel.setText(brand);
        modelLabel.setText(model);
        licensePlateLabel.setText(licensePlate);
        positionXLabel.setText(String.valueOf(x));
        positionYLabel.setText(String.valueOf(y));
        return car;
    }

    @FXML
    public void initialize() {
        System.out.println("HelloController initialized");

        Image carImage = new Image(Objects.requireNonNull(getClass().getResource("/car.jpg")).toExternalForm());
        carImageView.setImage(carImage);
        carImageView.setVisible(false);
        carImageView.setFitWidth(80);
        carImageView.setFitHeight(60);
        carImageView.setTranslateX(0);
        carImageView.setTranslateY(0);

        map.setOnMouseClicked(event -> {
            if (car != null) {
                double x = event.getX() - 30;
                double y = event.getY() - 30;
                Position newPosition = new Position(x, y);
                try {
                    car.driveTo(newPosition);
                } catch (GearBoxException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        map.setStyle("-fx-background-color: #c8dfc8;");

        selectCarComboBox.setOnAction(event -> {
            car = selectCarComboBox.getSelectionModel().getSelectedItem();
            if (car != null) {
                car.addListener(this);
                this.refresh();
            }
        });
        selectCarComboBox.setItems(cars);
        selectCarComboBox.setCellFactory(new Callback<ListView<Car>, ListCell<Car>>() {
            @Override
            public ListCell<Car> call(ListView<Car> param) {
                return new ListCell<Car>() {
                    @Override
                    protected void updateItem(Car item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getBrand() + " " + item.getModel());
                        }
                    }
                };
            }
        });
        selectCarComboBox.setButtonCell(new ListCell<Car>() {
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getBrand() + " " + item.getModel());
                }
            }
        });

        if (car != null) {
            car.start();
        }
        if (car == null) {
            stopCarButton.setDisable(true);
            startCarButton.setDisable(true);
            shiftUpButton.setDisable(true);
            shiftDownButton.setDisable(true);
            pressClutchButton.setDisable(true);
            releaseClutchButton.setDisable(true);
            increaseRpmButton.setDisable(true);
            decreaseRpmButton.setDisable(true);
        }
    }

    public void openAddCarWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/car/addCar.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Add new car");
        stage.show();
        AddCarController addCarController = loader.getController();
        addCarController.setParentController(this);
    }

    public void refresh() {
        if (car == null) return;
        brandLabel.setText(car.getBrand());
        modelLabel.setText(car.getModel());
        licensePlateLabel.setText(car.getLicensePlate());
        positionXLabel.setText(String.valueOf(car.getPosX()));
        positionYLabel.setText(String.valueOf(car.getPosY()));
        carWeightLabel.setText(String.valueOf(car.getTotalWeight()));
        gearBoxNameLabel.setText(String.valueOf(car.getGearBox().getName()));
        gearBoxPriceLabel.setText(String.valueOf(car.getGearBox().getPrice()));
        gearBoxWeightLabel.setText(String.valueOf(car.getGearBox().getGearBoxTotalWeight()));
        gearCountLabel.setText(String.valueOf(car.getGearBox().getGearCount()));
        currentGearLabel.setText(String.valueOf(car.getCurrentGear()));
        clutchNameLabel.setText(String.valueOf(car.getGearBox().getClutch().getName()));
        clutchPriceLabel.setText(String.valueOf(car.getGearBox().getPrice()));
        clutchWeightLabel.setText(String.valueOf(car.getGearBox().getClutch().getWeight()));
        engineNameLabel.setText(String.valueOf(car.getEngine().getName()));
        enginePriceLabel.setText(String.valueOf(car.getEngine().getPrice()));
        engineWeightLabel.setText(String.valueOf(car.getEngine().getWeight()));
        currentRpmLabel.setText(String.valueOf(car.getCurrentRpm()));
        maxRpmLabel.setText(String.valueOf(car.getMaxRpm()));
        engineStateLabel.setText(getEngineStateText());
        Platform.runLater(() -> {
            carImageView.setTranslateX(car.getPosX());
            carImageView.setTranslateY(car.getPosY());
        });
    }

    @FXML
    private void startCar() throws CarException, ClutchException {
        try {
            car.startCar();
            this.refresh();
        } catch (ClutchException e) {
            alertDialog("Clutch", e);
        }
        if (car.getGearBox().getClutch().isPressed()) {
            car.startEngine();
            engineStateLabel.setText("Running");
            this.refresh();
        }
    }

    @FXML
    public void stopCar() throws ClutchException, CarException {
        try {
            car.stopCar();
            this.refresh();
            System.out.println("Car stopped!");
        } catch (ClutchException e) {
            alertDialog("Clutch", e);
        }
        if (car.getGearBox().getClutch().isPressed()) {
            car.stopEngine();
            this.refresh();
        }
    }

    public String getEngineStateText() {
        return car.isEngineRunning() ? "Running" : "Stopped";
    }

    @FXML
    public void shiftUp() throws CarException, ClutchException, GearBoxException, EngineException {
        try {
            car.shiftUp();
            car.releaseClutch();
            while (car.getCurrentRpm() > 2000) {
                car.decreaseRpm();
            }
            car.pressClutch();
            this.refresh();
        } catch (CarException e) {
            alertDialog("Car", e);
        } catch (EngineException e1) {
            alertDialog("Engine", e1);
        } catch (GearBoxException e2) {
            alertDialog("GearBox", e2);
        } catch (ClutchException e3) {
            alertDialog("Clutch", e3);
        }
    }

    public void shiftDown() throws EngineException, ClutchException, GearBoxException, CarException {
        try {
            car.shiftDown();
            this.refresh();
            System.out.println("Shifting down!");
        } catch (CarException e) {
            alertDialog("Car", e);
        } catch (EngineException e1) {
            alertDialog("Engine", e1);
        } catch (GearBoxException e2) {
            alertDialog("GearBox", e2);
        } catch (ClutchException e3) {
            alertDialog("Clutch", e3);
        }
    }

    public void releaseClutch() {
        car.releaseClutch();
        this.refresh();
    }

    public void pressClutch() throws GearBoxException, ClutchException {
        car.pressClutch();
        this.refresh();
    }

    public void increaseRpm() throws EngineException, ClutchException, GearBoxException {
        try {
            car.increaseRpm();
            this.refresh();
        } catch (CarException e) {
            alertDialog("Car", e);
        } catch (ClutchException e1) {
            alertDialog("Clutch", e1);
        } catch (EngineException e2) {
            alertDialog("Engine", e2);
        }
    }

    public void decreaseRpm() throws CarException, EngineException {
        try {
            car.decreaseRpm();
            this.refresh();
        } catch (CarException e) {
            alertDialog("Car", e);
        } catch (EngineException e1) {
            alertDialog("Engine", e1);
        }
    }

    private void alertDialog(String component, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(component);
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }

    public void onCarDeleteButton(ActionEvent actionEvent) {
        if (car != null) {
            car.removeListener(this);
            cars.remove(car);
            car = null;
            selectCarComboBox.setItems(cars);
            if (!cars.isEmpty()) {
                selectCarComboBox.getSelectionModel().selectFirst();
            } else {
                // czyścimy wszystkie etykiety gdy lista jest pusta
                carImageView.setVisible(false);
                brandLabel.setText(null);
                modelLabel.setText(null);
                licensePlateLabel.setText(null);
                positionXLabel.setText(null);
                positionYLabel.setText(null);
                carWeightLabel.setText(null);
                gearBoxNameLabel.setText(null);
                gearBoxPriceLabel.setText(null);
                gearBoxWeightLabel.setText(null);
                gearCountLabel.setText(null);
                currentGearLabel.setText(null);
                clutchNameLabel.setText(null);
                clutchPriceLabel.setText(null);
                clutchWeightLabel.setText(null);
                engineNameLabel.setText(null);
                enginePriceLabel.setText(null);
                engineWeightLabel.setText(null);
                currentRpmLabel.setText(null);
                maxRpmLabel.setText(null);
                engineStateLabel.setText(null);
            }
        }
    }

    @Override
    public void update() {}
}