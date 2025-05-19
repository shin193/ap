package mid_project.part_1;

import java.time.LocalDate;

public class Loan {
    private final Book book;
    private final Student student ;
    private final Librarian borrowedLibrarians;
    private Librarian returnedLibrarians;
    private final LocalDate borrowedDate;
    private LocalDate returnedDate;
    private final LocalDate dueDate;
    public Loan (Book book, Student student, Librarian borrowedLibrarians, LocalDate borrowedDate, LocalDate dueDate) {
        this.book = book;
        this.student = student;
        this.borrowedLibrarians = borrowedLibrarians;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
    }

    public Book getBook() {
        return book;
    }
    public Student getStudent() {
        return student;
    }
    public Librarian getBorrowedLibrarians() {
        return borrowedLibrarians;
    }
    public Librarian getReturnedLibrarians() {
        return returnedLibrarians;
    }
    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }
    public LocalDate getReturnedDate() {
        return returnedDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void returnBook(LocalDate returnedDate, Librarian returnedLibrarians ) {
        this.returnedDate = returnedDate;
        this.returnedLibrarians = returnedLibrarians;

    }
    public boolean isReturned () {
        return returnedDate != null;
    }
    public boolean isOverdue() {
        return returnedDate !=null && returnedDate.isAfter(dueDate);
    }

    public String toString() {
        return "loan : "+ book.getTitle() +",to student : "+ student.getFullName()+"on "
                +borrowedDate+", due "+dueDate+(isReturned() ? ", returned: " + returnedDate : ", not returned");
    }

}
