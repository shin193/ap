package Exercise.Ex4.E3_10;

public class CashRegister {
    private double total;
    private String items;

    public CashRegister() {
        total = 0;
        items = "";
    }

    public void addItem(double price) {
        total += price;
        items = items.concat(String.valueOf(price)).concat("\n");
    }

    public void printReceipt() {
        System.out.print("Items:\n" + items);
        System.out.println("Total: " + total);
    }
}
