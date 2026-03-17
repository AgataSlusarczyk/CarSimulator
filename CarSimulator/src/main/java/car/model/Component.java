package car.model;

public class Component {
    private String name = "";
    private int weight = 0;
    private int price = 0;

    public Component(String name, int weight, int price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName(){
        return name;
    }
    public int getWeight(){
        return weight;
    }
    public int getPrice(){
        return price;
    }

}
