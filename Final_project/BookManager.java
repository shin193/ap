package Final_project;

import java.util.*;

public class BookManager {
    private List<Book> books = new ArrayList<>();
    private Map<String, Book> bookMapID = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public BookManager() {
        this.books = FileHandling.loadDataBk();
        if (books.isEmpty()) {
            addDefaultBooks();
        }
        initializeMaps();
    }

    private void initializeMaps() {
        for (Book book : books) {
            bookMapID.put(book.getId(), book);
        }
    }

    private void addDefaultBooks() {
        Book book1 = new Book("Book 1", "0001", "author1", 2000);
        bookMapID.put("0001", book1);
        Book book2 = new Book("Book 2", "0002", "author2", 2001);
        bookMapID.put("0002", book2);

        books.add(book1);
        books.add(book2);
        FileHandling.saveDataBk(books);
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
        bookMapID.put(id, newBook);
        books.add(newBook);
        FileHandling.saveDataBk(books);
        System.out.println("Book added successfully.");
    }

    public void editBookInfo(String bookId) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book with ID " + bookId + " not found.");
            return;
        }

        System.out.println("Editing book: " + book.getTitle());
        System.out.println("1. Edit title");
        System.out.println("2. Edit author");
        System.out.println("3. Edit publication year");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();
        switch (choice) {
                case "1":
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    if (newTitle.trim().isEmpty()) {
                        System.out.println("Title cannot be empty!");
                        return;
                    }
                    book.setTitle(newTitle);
                    break;
                case "2":
                    System.out.print("Enter new author: ");
                    String newAuthor = scanner.nextLine();
                    if (newAuthor.trim().isEmpty()) {
                        System.out.println("Author cannot be empty!");
                    }
                    book.setAuthor(newAuthor);
                    break;
                case "3":
                    System.out.print("Enter new publication year: ");
                    String newYear = scanner.nextLine();
                    if (newYear.trim().isEmpty()) {
                        System.out.println("Publication year cannot be empty!");
                        return;
                    }
                    book.setPublishYear(Integer.parseInt(newYear));
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }
        FileHandling.saveDataBk(books);
        System.out.println("Book edited successfully.");
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
        return bookMapID.get(id);
    }

    public void requestBorrow(String bookId, Student student) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is not available.");
            return;
        }
        System.out.println("How many days do you want to borrow the book?");
        int days;
        try {
            days = scanner.nextInt();
        }catch (InputMismatchException e) {
            System.out.println("Please enter an number.");
            return;
        }
        book.setBorrowRequested(true , student.getStudentId(), days);
        FileHandling.saveDataBk(books);
        System.out.println("Book requested successfully by " + student.getName());
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
        return bookMapID.containsKey(id);
    }
    public int getNumberOfBooks() { return books.size(); }

    public List<Book> getAllBooks() {
        System.out.println("\n******* All Books *******");
        return books;
    }

}