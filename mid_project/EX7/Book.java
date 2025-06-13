package mid_project.EX7;

import java.util.Scanner;

public class Book{
    private String title;
    private String author;
    private int publishDate;
    private int bookPage;
    static Scanner scanner = new Scanner(System.in);
    public Book(String title, String author, int publishDate, int bookPage) {
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.bookPage = bookPage;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public int getPublishDate() {
        return publishDate;
    }
    public int getBookPage() {
        return bookPage;
    }
    public String toString() {
        return "title: " + title + ", author: " + author + ", publishDate: " + publishDate + ", bookPage: " + bookPage;
    }
}