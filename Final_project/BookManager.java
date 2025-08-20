package Final_project;

import java.util.ArrayList;
import java.util.List;

public class BookManager {
    private List<Book> books = new ArrayList<>();

    public BookManager() {
        this.books = FileHandling.loadDataBk();
    }

    public void addBook(String title, String id, String author) {
        if (isBookIdTaken(id)) {
            System.out.println("This book ID already exists. Please choose a different ID.");
            return;
        }

        Book newBook = new Book(title, id, author);
        books.add(newBook);
        FileHandling.saveDataBk(books);
        System.out.println("Book added successfully.");
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public Book findBookById(String id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void borrowBook(String bookId, Student student) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is not available for borrowing.");
            return;
        }
        book.setAvailable(false);
        FileHandling.saveDataBk(books);
        System.out.println("Book borrowed successfully by " + student.getName());
    }

    public void returnBook(String bookId) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (book.isAvailable()) {
            System.out.println("Book is already available.");
            return;
        }
        book.setAvailable(true);
        FileHandling.saveDataBk(books);
        System.out.println("Book returned successfully.");
    }

    private boolean isBookIdTaken(String id) {
        return books.stream().anyMatch(b -> b.getId().equals(id));
    }
}