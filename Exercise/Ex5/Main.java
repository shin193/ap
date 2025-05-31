package Exercise.Ex5;


public class Main {
    public static void main(String[] args) {
        StringCounter list = new StringCounter();
        //////////////////////////////////
        list.addItem("hello");
        list.addItem("patrick");
        list.addItem("patrick");
        list.addItem("patrick");
        list.addItem("hello");
        list.addItem("sandi");
        list.addItem("spongebob");
        list.addItem("spongebob");
        list.addItem("spongebob");
        list.addItem("spongebob");
        list.addItem("patrick");
        list.addItem("patrick");
        list.addItem("patrick");
        list.addItem("spongebob");
        /////////////////////////////////
        for (String s : list.getCounts()) {
            System.out.println(s);
        }


    }
}