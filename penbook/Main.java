package penbook;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Book> books = new ArrayList<Book>();
        ArrayList <pen> pens  = new ArrayList<pen>();
        Book b1=new Book(2,"aa");
        Book b2=new Book(3,"bb");
        books.add(b1);
        books.add(b2);
        pen p1=new pen(3,"red","2");
        pens.add(p1);
        pen p2=new pen(4,"green","3");
        pens.add(p2);
        for(Book b:books){
            System.out.println(b.showInfo());
        }
        for(pen p:pens){
            System.out.println(p.showInfo());
        }
    }
}