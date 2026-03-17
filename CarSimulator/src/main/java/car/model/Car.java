package car.model;

import car.app.CarSimulatorController;
import car.exception.CarException;
import car.exception.ClutchException;
import car.exception.EngineException;
import car.exception.GearBoxException;
import car.listener.Listener;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Car extends Thread {
    private boolean isRunning = false;
    private final String licensePlate;
    private final String model;
    private final String brand;
    private final int maxSpeed;
    private final double weight;

    private final GearBox gearBox;
    private final Engine engine;
    private Position currentPosition = new Position(0, 0);
    private CarSimulatorController controller;
    private Position destination;
    private final List<Listener> listeners = new ArrayList<>();


    public Car(int gearCount, int maxRpm, String licensePlate, String brand, String model, int maxSpeed, double weight, String engineName, String gearBoxName, String clutchName, int engineWeight, int gearBoxWeight, int clutchWeight, int enginePrice, int gearBoxPrice, int clutchPrice) {
        engine = new Engine(engineName, engineWeight, enginePrice, maxRpm);
        gearBox = new GearBox(gearCount, gearBoxName, clutchName, gearBoxWeight, clutchWeight, gearBoxPrice, clutchPrice);
        isRunning = false;
        this.licensePlate = licensePlate;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.brand = brand;
        this.weight = weight;
        this.start();
    }

    public void startCar() {
        if (gearBox.getClutch().isPressed()) {
            isRunning = true;
            engine.start();
        } else {
            throw new ClutchException("Clutch not pressed - cannot start the car and engine");
        }
    }

    public void stopCar() {
        if (gearBox.getClutch().isPressed()) {
            isRunning = false;
            engine.stop();
            gearBox.resetGear();
        } else {
            throw new ClutchException("Clutch not pressed - cannot stop the car and engine");
        }
    }

    public void shiftUp() throws GearBoxException, CarException, EngineException {
        if (isRunning) {
            if (engine.getRpm() >= 2500) {
                gearBox.increaseGear();
            } else {
                throw new EngineException("RPM too low - cannot shift up!");
            }
        } else {
            throw new CarException("Car is turned off");
        }
    }

    public void shiftDown() throws GearBoxException, CarException, EngineException {
        if (isRunning) {
            if (engine.getRpm() <= 1500) {
                gearBox.decreaseGear();
            } else {
                throw new EngineException("RPM too high - lower it before shifting down");
            }
        } else {
            throw new CarException("Car is turned off");
        }
    }

    public void pressClutch() throws GearBoxException, ClutchException {
        gearBox.getClutch().press();
    }

    public void releaseClutch() {
        gearBox.getClutch().release();
    }

    public boolean isClutchPressed() {
        return gearBox.getClutch().isPressed();
    }

    public void increaseRpm() throws CarException {
        if (isRunning) {
            if (!isClutchPressed()) {
                engine.increaseRpm();
            } else {
                throw new ClutchException("Clutch is pressed");
            }
        } else {
            throw new CarException("Car is turned off - cannot increase RPM");
        }
    }

    public void decreaseRpm() throws CarException, EngineException {
        if (isRunning) {
            if (!isClutchPressed()) {
                if (engine.getRpm() > 800) {
                    engine.decreaseRpm();
                } else {
                    throw new EngineException("RPM too low - cannot decrease further!");
                }
            } else {
                throw new ClutchException("Clutch is pressed");
            }
        } else {
            throw new CarException("Car is turned off - cannot decrease RPM!");
        }
    }

    public void startEngine() throws CarException, ClutchException {
        if (gearBox.getClutch().isPressed()) {
            if (isRunning) {
                engine.start();
            } else {
                throw new CarException("Car is turned off");
            }
        } else {
            throw new ClutchException("Clutch is not pressed");
        }
    }

    public void stopEngine() throws CarException, ClutchException {
        if (gearBox.getClutch().isPressed()) {
            if (!isRunning) {
                engine.stop();
            } else {
                throw new CarException("Car is still running");
            }
        } else {
            throw new ClutchException("Clutch is not pressed");
        }
    }

    public int getCurrentRpm() {
        return engine.getRpm();
    }

    public int getMaxRpm() {
        return engine.getMaxRpm();
    }

    public boolean isEngineRunning() {
        return engine.getEngineState();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void driveTo(Position newPosition) throws GearBoxException {
        this.destination = newPosition;
    }

    public double getTotalWeight() {
        return engine.getWeight() + gearBox.getGearBoxTotalWeight() + this.weight;
    }

    public int getSpeed() {
        if (!isRunning || gearBox.getCurrentGear() == 0) {
            return 0;
        }
        return (engine.getRpm() / 400) * gearBox.getCurrentGear() * 5;
    }

    public void setController(CarSimulatorController controller) {
        this.controller = controller;
    }

    public GearBox getGearBox() {
        return gearBox;
    }

    public Engine getEngine() {
        return engine;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getPosX() {
        return (int) currentPosition.getX();
    }

    public int getPosY() {
        return (int) currentPosition.getY();
    }

    public int getCurrentGear() {
        return gearBox.getCurrentGear();
    }

    @Override
    public void run() {
        double deltaT = 0.1;

        while (true) {
            if (destination != null) {
                if (abs(destination.getX() - currentPosition.getX()) > 5 && abs(destination.getY() - currentPosition.getY()) > 5) {
                    double distance = Math.sqrt(Math.pow(destination.getX() - currentPosition.getX(), 2) +
                            Math.pow(destination.getY() - currentPosition.getY(), 2));
                    double dx = this.getSpeed() * deltaT * (destination.getX() - currentPosition.getX()) / distance;
                    double dy = this.getSpeed() * deltaT * (destination.getY() - currentPosition.getY()) / distance;

                    currentPosition.setX(currentPosition.getX() + dx);
                    currentPosition.setY(currentPosition.getY() + dy);
                    this.notifyListeners();
                    if (controller != null) {
                        Platform.runLater(() -> {
                            if (controller != null) { // <- podwójne sprawdzenie dla bezpieczeństwa wątków
                                controller.refresh();
                            }
                        });
                    }
                }
            }
            try {
                sleep(40);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }
}