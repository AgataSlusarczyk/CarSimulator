package car.model;

import car.exception.EngineException;

public class Engine extends Component {
    private int maxRpm = 0;
    private int rpm = 0;
    private boolean isEngineRunning = false;

    public Engine(String name, int weight, int price, int maxRpm) {
        super(name, weight, price);
        this.maxRpm = maxRpm;
    }

    public void start() {
        rpm = 800;
        isEngineRunning = true;
    }

    public void stop() {
        rpm = 0;
        isEngineRunning = false;
    }

    public void increaseRpm() {
        if (rpm < maxRpm) {
            rpm += 100;
        } else {
            throw new EngineException("Maximum RPM already reached");
        }
    }

    public void decreaseRpm() {
        if (rpm <= 0) {  // BUGFIX: było rpm < 0, nigdy nie wchodziło do tego bloku
            rpm = 0;
        } else {
            rpm -= 100;
        }
    }

    public int getRpm() {
        return rpm;
    }

    public int getMaxRpm() {
        return maxRpm;
    }

    public boolean getEngineState() {
        return isEngineRunning;
    }
}