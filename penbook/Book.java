package penbook;

public class Book implements info{
    int price;
    String title;
    public Book(int price, String title) {
        this.price = price;
        this.title = title;
    }


    @Override
    public String showInfo() {
        return  price+ " " + title;
    }
}

