package Final_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookManager {
    private List<Book> books = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public BookManager() {
        this.books = FileHandling.loadDataBk();
    }

    public List<Book> searchBooks(String title, String author, Integer publicationYear) {
        List<Book> results = new ArrayList<>();

        for (Book book : books) {
            boolean matches = true;

            if (title != null && !title.isEmpty()) {
                if (!book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    matches = false;
                }
            }
            if (matches && author != null && !author.isEmpty()) {
                if (!book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                    matches = false;
                }
            }
            if (matches && publicationYear != null) {
                if (book.getPublishYear() != publicationYear) {
                    matches = false;
                }
            }
            if (matches) {
                results.add(book);
            }
        }

        return results;
    }

    public void addBook(String title, String id, String author , int year) {
        if (isBookIdTaken(id)) {
            System.out.println("This book ID already exists. Please choose a different ID.");
            return;
        }

        Book newBook = new Book(title, id, author , year);
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
    public int getNumberOfBooks() { return books.size(); }
}