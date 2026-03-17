package car.model;

public class Clutch extends Component {
    private boolean isPressed = false; // not pressed

    public Clutch(String name, int weight, int price) {
        super(name, weight, price);
    }

    public void press() {
        isPressed = true;
    }

    public void release() {
        isPressed = false;
    }

    public boolean isPressed() {
        return isPressed;
    }
}