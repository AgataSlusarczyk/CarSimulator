package car.app;

import car.model.Clutch;
import car.model.Engine;
import car.model.GearBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddCarController {

    @FXML private TextField brandTextField;
    @FXML private TextField modelTextField;
    @FXML private TextField licensePlateTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField maxSpeedTextField;

    @FXML private RadioButton customGearBoxRadio;
    @FXML private RadioButton predefGearBoxRadio;
    @FXML private RadioButton customClutchRadio;
    @FXML private RadioButton predefClutchRadio;
    @FXML private RadioButton customEngineRadio;
    @FXML private RadioButton predefEngineRadio;

    @FXML private VBox gearBoxInputGroup;
    @FXML private VBox clutchInputGroup;
    @FXML private VBox engineInputGroup;

    @FXML private TextField gearBoxNameTextField;
    @FXML private TextField gearBoxPriceTextField;
    @FXML private TextField gearBoxWeightTextField;
    @FXML private TextField gearCountTextField;
    @FXML private ComboBox<GearBox> selectGearBoxComboBox;

    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchPriceTextField;
    @FXML private TextField clutchWeightTextField;
    @FXML private ComboBox<Clutch> selectClutchComboBox;

    @FXML private TextField engineNameTextField;
    @FXML private TextField enginePriceTextField;
    @FXML private TextField engineWeightTextField;
    @FXML private TextField maxRpmTextField;
    @FXML private ComboBox<Engine> selectEngineComboBox;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private CarSimulatorController parentController;

    @FXML
    public void initialize() {
        ToggleGroup gearBoxGroup = new ToggleGroup();
        customGearBoxRadio.setToggleGroup(gearBoxGroup);
        predefGearBoxRadio.setToggleGroup(gearBoxGroup);

        ToggleGroup clutchGroup = new ToggleGroup();
        customClutchRadio.setToggleGroup(clutchGroup);
        predefClutchRadio.setToggleGroup(clutchGroup);

        ToggleGroup engineGroup = new ToggleGroup();
        customEngineRadio.setToggleGroup(engineGroup);
        predefEngineRadio.setToggleGroup(engineGroup);

        customGearBoxRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            gearBoxInputGroup.setDisable(!newVal);
            selectGearBoxComboBox.setDisable(newVal);
        });

        customClutchRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            clutchInputGroup.setDisable(!newVal);
            selectClutchComboBox.setDisable(newVal);
        });

        customEngineRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            engineInputGroup.setDisable(!newVal);
            selectEngineComboBox.setDisable(newVal);
        });

        initializeComboBoxes();
    }

    private void initializeComboBoxes() {
        selectGearBoxComboBox.setItems(FXCollections.observableArrayList(
                new GearBox(6, "C30 1.6 3M5R7002NE", 12, 3000),
                new GearBox(5, "B20 1.4 2M4R5001NE", 10, 2500)
        ));
        selectGearBoxComboBox.setCellFactory(param -> new ListCell<GearBox>() {
            @Override
            protected void updateItem(GearBox item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        selectGearBoxComboBox.setButtonCell(new ListCell<GearBox>() {
            @Override
            protected void updateItem(GearBox item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        selectClutchComboBox.setItems(FXCollections.observableArrayList(
                new Clutch("SP240, Sport Clutch", 8, 1500),
                new Clutch("SP180, Standard Clutch", 6, 1200)
        ));
        selectClutchComboBox.setCellFactory(param -> new ListCell<Clutch>() {
            @Override
            protected void updateItem(Clutch item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        selectClutchComboBox.setButtonCell(new ListCell<Clutch>() {
            @Override
            protected void updateItem(Clutch item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        selectEngineComboBox.setItems(FXCollections.observableArrayList(
                new Engine("2.0T, Turbo Engine", 25, 3000, 6000),
                new Engine("1.8N, Natural Engine", 20, 2500, 5000)
        ));
        selectEngineComboBox.setCellFactory(param -> new ListCell<Engine>() {
            @Override
            protected void updateItem(Engine item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        selectEngineComboBox.setButtonCell(new ListCell<Engine>() {
            @Override
            protected void updateItem(Engine item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    @FXML
    private void onConfirmButton() {
        try {
            String brand = brandTextField.getText();
            String model = modelTextField.getText();
            String licensePlate = licensePlateTextField.getText();
            int weight = Integer.parseInt(weightTextField.getText());
            int maxSpeed = Integer.parseInt(maxSpeedTextField.getText());

            String gearBoxName;
            int gearBoxPrice;
            int gearBoxWeight;
            int gearCount;

            if (customGearBoxRadio.isSelected()) {
                gearBoxName = gearBoxNameTextField.getText();
                gearBoxPrice = Integer.parseInt(gearBoxPriceTextField.getText());
                gearBoxWeight = Integer.parseInt(gearBoxWeightTextField.getText());
                gearCount = Integer.parseInt(gearCountTextField.getText());
            } else {
                GearBox selectedGearBox = selectGearBoxComboBox.getValue();
                gearBoxName = selectedGearBox.getName();
                gearBoxPrice = selectedGearBox.getPrice();
                gearBoxWeight = selectedGearBox.getWeight();
                gearCount = selectedGearBox.getGearCount();
            }

            String clutchName;
            int clutchPrice;
            int clutchWeight;

            if (customClutchRadio.isSelected()) {
                clutchName = clutchNameTextField.getText();
                clutchPrice = Integer.parseInt(clutchPriceTextField.getText());
                clutchWeight = Integer.parseInt(clutchWeightTextField.getText());
            } else {
                Clutch selectedClutch = selectClutchComboBox.getValue();
                clutchName = selectedClutch.getName();
                clutchPrice = selectedClutch.getPrice();
                clutchWeight = selectedClutch.getWeight();
            }

            String engineName;
            int enginePrice;
            int engineWeight;
            int maxRpm;

            if (customEngineRadio.isSelected()) {
                engineName = engineNameTextField.getText();
                enginePrice = Integer.parseInt(enginePriceTextField.getText());
                engineWeight = Integer.parseInt(engineWeightTextField.getText());
                maxRpm = Integer.parseInt(maxRpmTextField.getText());
            } else {
                Engine selectedEngine = selectEngineComboBox.getValue();
                engineName = selectedEngine.getName();
                enginePrice = selectedEngine.getPrice();
                engineWeight = selectedEngine.getWeight();
                maxRpm = selectedEngine.getMaxRpm();
            }

            parentController.createCar(clutchName, clutchWeight, clutchPrice, gearCount,
                    gearBoxPrice, gearBoxWeight, gearBoxName, maxRpm, engineName,
                    engineWeight, enginePrice, licensePlate, model, brand, weight, 0, 0, maxSpeed);

            parentController.addCarToList(clutchName, clutchWeight, clutchPrice, gearCount,
                    gearBoxPrice, gearBoxWeight, gearBoxName, maxRpm, engineName,
                    engineWeight, enginePrice, licensePlate, model, brand, weight, 0, 0, maxSpeed);

            parentController.stopCarButton.setDisable(false);
            parentController.startCarButton.setDisable(false);
            parentController.shiftUpButton.setDisable(false);
            parentController.shiftDownButton.setDisable(false);
            parentController.pressClutchButton.setDisable(false);
            parentController.releaseClutchButton.setDisable(false);
            parentController.increaseRpmButton.setDisable(false);
            parentController.decreaseRpmButton.setDisable(false);

            onCancelButton();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please check all numeric fields and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    public void onCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setParentController(CarSimulatorController controller) {
        this.parentController = controller;
    }
}