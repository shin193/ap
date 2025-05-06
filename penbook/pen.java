package penbook;

public class pen implements info{
    int price;
    String color;
    String brand;
    public pen(int price, String title,String brand) {
        this.price = price;
        this.color = title;
        this.brand = brand;
    }


    @Override
    public String showInfo() {
        return price + " " + color + " " + brand;
    }
}
