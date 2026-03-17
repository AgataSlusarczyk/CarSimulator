package car.model;

import car.exception.ClutchException;
import car.exception.GearBoxException;

public class GearBox extends Component {
    private int currentGear = 1;       // 0 - neutral, 1-5 gears, 6 reverse
    private final int gearCount;
    private int currentRatio = 0;

    private Clutch clutch;

    public GearBox(int gearCount, String name, String clutchName, int gearBoxWeight, int clutchWeight, int gearBoxPrice, int clutchPrice) {
        super(name, gearBoxWeight, gearBoxPrice);
        this.gearCount = gearCount;
        this.clutch = new Clutch(clutchName, clutchWeight, clutchPrice);
    }

    public GearBox(int gearCount, String name, int gearBoxWeight, int gearBoxPrice) {
        super(name, gearBoxWeight, gearBoxPrice);
        this.gearCount = gearCount;
    }

    public void increaseGear() throws GearBoxException, ClutchException {
        if (clutch.isPressed()) {
            if (currentGear < gearCount) {
                currentGear++;
            } else {
                throw new GearBoxException("Cannot shift up - already in highest gear!");
            }
        } else {
            throw new ClutchException("Clutch not pressed - cannot shift up");
        }
    }

    public void decreaseGear() throws GearBoxException {
        if (clutch.isPressed()) {
            if (currentGear > 0) {
                currentGear--;
            } else {
                throw new GearBoxException("Cannot shift down - already in neutral!");
            }
        } else {
            throw new ClutchException("Clutch not pressed - cannot shift down");
        }
    }

    public void resetGear() {
        currentGear = 0;
    }

    public int getCurrentGear() {
        return currentGear;
    }

    public int getCurrentRatio() {
        return currentRatio;
    }

    public int getGearBoxTotalWeight() {
        return this.getWeight() + clutch.getWeight();
    }

    public int getGearCount() {
        return gearCount;
    }

    public Clutch getClutch() {
        return clutch;
    }
}