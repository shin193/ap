package mid_project.part_1;

import java.util.ArrayList;
import java.util.List;


public class Library {
    private final String name;
    private final List<Book> books;
    private final List<Student> students;
    private final List<Librarian> Librarians;
    private final List<Loan> loans;
    private final Manager manager;

    public Library(String name,Manager manager,List<Librarian> librarians) {
        this.name = name;
        this.Librarians = librarians;
        this.books = new ArrayList<Book>();
        this.students = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.manager = manager;
    }
    public Manager getManager() {
        return manager;
    }
    public String getName() {
        return name;
    }
    public List<Book> getBooks() {
        return books;
    }
    public List<Student> getStudents() {
        return students;
    }
    public List<Librarian> getLibrarians() {
        return Librarians;
    }
    public List<Loan> getLoans() {
        return loans;
    }
    public void addStudent(Student student) {
        students.add(student);
    }
    public void addBook(Book book) {
        books.add(book);
    }
    public void addLoan(Loan loan) {
        loans.add(loan);
    }
    public void addLibrarian(Librarian librarian) {Librarians.add(librarian);}

}