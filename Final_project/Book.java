package Final_project;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private final String title;
    private final String id;
    private final String author;
    private final int publishYear;
    private boolean isAvailable;
    private LocalDate borrowStartDate;
    private LocalDate borrowEndDate;
    private String borrowedByStudentId;

    public Book(String title, String id, String author , int publisher) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.publishYear = publisher;
        this.isAvailable = true;
        this.borrowStartDate = null;
        this.borrowEndDate = null;
        this.borrowedByStudentId = null;
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
    public int getPublishYear() {
        return publishYear ;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDate getBorrowStartDate() {
        return borrowStartDate;
    }
    public LocalDate getBorrowEndDate() {
        return borrowEndDate;
    }
    public String getBorrowedByStudentId() {
        return borrowedByStudentId;
    }
    public void setBorrowDates(LocalDate borrowStartDate, LocalDate borrowEndDate , String borrowedByStudentId) {
        this.borrowStartDate = borrowStartDate;
        this.borrowEndDate = borrowEndDate;
        this.borrowedByStudentId = borrowedByStudentId;
        isAvailable = false;

    }

    public void returnBook() {
        this.borrowStartDate = null;
        this.borrowEndDate = null;
        this.borrowedByStudentId = null;
        isAvailable = true;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Not Available , Borrowed by " + borrowedByStudentId + "from " + borrowStartDate + " to " + borrowEndDate;

        return "---title : " + title + "\n---id :" + id + "\n---author :" + author + "\n---status :" + status;
    }

}
