package Final_project;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private String title;
    private String id;
    private String author;
    private int publishYear;
    private boolean isAvailable;
    private LocalDate borrowStartDate;
    private LocalDate borrowEndDate;
    private LocalDate requestDate;
    private String borrowedByStudentId;
    private boolean borrowRequested;
    private String requestedByStudentId;
    private int borrowDays;
    private static final long serialVersionUID = 1L;
    private LocalDate receivedDate;
    private boolean isReceived;
    private boolean isReturned;

    public Book(String title, String id, String author , int publisher) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.publishYear = publisher;
        this.isAvailable = true;
        this.borrowStartDate = null;
        this.borrowEndDate = null;
        this.borrowedByStudentId = null;
        this.borrowRequested = false;
        this.requestedByStudentId = null;
        this.borrowDays = 0;
        this.requestDate = null;
        this.receivedDate = null;
        this.isReceived = false;
        this.isReturned = false;
    }

    public void markAsReceived() {
        this.isReceived = true;
        this.isReturned = false;
        this.receivedDate = LocalDate.now();
    }
    public LocalDate getReceivedDate() {
        return receivedDate;
    }
    public boolean isReceived() {
        return isReceived;
    }
    public void setBorrowRequested(boolean borrowRequested , String studentId , int borrowDays) { this.borrowRequested = borrowRequested;
    this.requestedByStudentId = studentId; this.borrowDays = borrowDays; this.requestDate = LocalDate.now() ;isAvailable = false;}
    public void approveBorrowRequest() {
        borrowStartDate = LocalDate.now();
        borrowEndDate = LocalDate.now().plusDays(borrowDays);
        this.borrowedByStudentId = this.requestedByStudentId;
        this.borrowRequested = false;
        this.requestedByStudentId = null;
        isAvailable = false;
        isReturned = false;
    }
    public String getRequestedByStudentId() {
        return requestedByStudentId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getPublishYear() {
        return publishYear ;
    }
    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }
    public int getBorrowDays() {
        return borrowDays;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public boolean isRequested() {
        return borrowRequested;
    }
    public boolean isReturned() {
        return isReturned;
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

    public void returnBook() {
        this.borrowStartDate = null;
        this.borrowEndDate = null;
        this.borrowedByStudentId = null;
        this.isAvailable = true;
        this.isReturned = true;
        this.isReceived = false;
    }
    @Override
    public String toString() {
        String status;
        if (!isAvailable) {
            if (borrowStartDate == null) {
                status = "Requested by Student Id: " + getRequestedByStudentId();
            } else {
                String receivedInfo = isReceived ?
                        " | Received on: " + receivedDate :
                        " | Not yet received";
                status = "Not Available, Borrowed by Student Id: " + borrowedByStudentId +
                        " from " + borrowStartDate + " to " + borrowEndDate + receivedInfo;
            }
        } else {
            status = "Available";
        }

        return "| Title: " + title + "\n| ID: " + id + "\n| Author: " + author +
                "\n| Status: " + status + "\n*=========================*";
    }

}
