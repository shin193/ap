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
    }
    public boolean isBorrowRequested() { return borrowRequested; }
    public void setBorrowRequested(boolean borrowRequested , String studentId , int borrowDays) { this.borrowRequested = borrowRequested;
    this.requestedByStudentId = studentId; this.borrowDays = borrowDays; this.requestDate = LocalDate.now() ;isAvailable = false;}

    public void approveBorrowRequest() {
        borrowStartDate = LocalDate.now();
        borrowEndDate = LocalDate.now().plusDays(borrowDays);
        this.borrowedByStudentId = this.requestedByStudentId;
        this.borrowRequested = false;
        this.requestedByStudentId = null;
        isAvailable = false;
    }

    public void rejectBorrowRequest() {
        this.borrowRequested = false;
        this.requestedByStudentId = null;
        this.borrowDays = 0;
        this.requestDate = null;
        isAvailable = true;
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
    public LocalDate getRequestDate() {
        return requestDate;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    public boolean isRequested() {
        return borrowRequested;
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
        String status;
        if (!isAvailable) {
            if (borrowStartDate == null) {
                status = "Requested by Student Id : "+getRequestedByStudentId();
            }
            else {
                status= "Not Available , Borrowed by " + borrowedByStudentId + " from " + borrowStartDate + " to " + borrowEndDate;
            }
        }
        else {
            status = "Available";
        }

        return "---title : " + title + "\n---id :" + id + "\n---author :" + author + "\n---status :" + status+"\n*=========================*";
    }


}
