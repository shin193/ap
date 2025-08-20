package Final_project;

import java.io.Serializable;

public class Book implements Serializable {
    private final String title;
    private final String id;
    private final String author;
    private boolean isAvailable;
    public Book(String title, String id, String author) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.isAvailable = true;
    }
    public String getTitle() {
        return title;
    }
    public String getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    @Override
    public String toString() {
        return "-- title=" + title + "-- id=" + id + "-- author=" + author + "-- isAvailable=" + isAvailable;
    }

}
