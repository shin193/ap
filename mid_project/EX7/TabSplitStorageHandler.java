package mid_project.EX7;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TabSplitStorageHandler implements StorageHandler {
    @Override
    public void saveLibrary(Library library) {
        saveStudents(library.getStudents());
        saveBooks(library.getBooks());
        saveLibrarians(library.getLibrarians());
        saveLoans(library.getLoans());
    }

    private void saveStudents(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            for (Student s : students) {
                writer.println(String.join("\t",
                        s.getFirstName(),
                        s.getLastName(),
                        s.getStudentId(),
                        s.getMajor(),
                        s.getRegisterDate().toString()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    private void saveBooks(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("books.txt"))) {
            for (Book b : books) {
                writer.println(String.join("\t",
                        b.getTitle(),
                        b.getAuthor(),
                        String.valueOf(b.getPublishDate()),
                        String.valueOf(b.getBookPage())
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void saveLibrarians(List<Librarian> librarians) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("librarians.txt"))) {
            for (Librarian l : librarians) {
                writer.println(String.join("\t",
                        l.getFirstName(),
                        l.getLastName(),
                        l.getEmployeeId()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving librarians: " + e.getMessage());
        }
    }

    private void saveLoans(List<Loan> loans) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("loans.txt"))) {
            for (Loan loan : loans) {
                writer.println(String.join("\t",
                        loan.getBook().getTitle(),
                        loan.getStudent().getStudentId(),
                        loan.getBorrowedDate().toString(),
                        loan.getDueDate().toString(),
                        loan.getBorrowedLibrarians().getEmployeeId(),
                        loan.isReturned() ? loan.getReturnedDate().toString() : "null",
                        loan.isReturned() ? loan.getReturnedLibrarians().getEmployeeId() : "null"
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving loans: " + e.getMessage());
        }
    }

    @Override
    public Library loadLibrary() {
        Manager manager = new Manager("arshia", "karimi", "12344321", "computer engineering");
        List<Librarian> librarians = loadLibrarians();
        librarians.add(new Librarian("mohammad", "heydari", "0001"));
        librarians.add(new Librarian("jamal", "gondaviz", "0002"));

        Library library = new Library("University Library", manager, librarians);
        library.getStudents().addAll(loadStudents());
        library.getBooks().addAll(loadBooks());
        library.getLoans().addAll(loadLoans(library));
        return library;
    }

    private List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 5) {
                    students.add(new Student(
                            parts[0], parts[1], parts[2], parts[3], LocalDate.parse(parts[4])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    private List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    books.add(new Book(
                            parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    private List<Librarian> loadLibrarians() {
        List<Librarian> librarians = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("librarians.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    librarians.add(new Librarian(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading librarians: " + e.getMessage());
        }
        return librarians;
    }

    private List<Loan> loadLoans(Library library) {
        List<Loan> loans = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("loans.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 5) {
                    Book book = findBookByTitle(library, parts[0]);
                    Student student = findStudentById(library, parts[1]);
                    Librarian borrowedLibrarian = findLibrarianById(library, parts[4]);

                    if (book != null && student != null && borrowedLibrarian != null) {
                        Loan loan = new Loan(book, student, borrowedLibrarian,
                                LocalDate.parse(parts[2]), LocalDate.parse(parts[3]));

                        if (parts.length >= 7 && !"null".equals(parts[5])) {
                            Librarian returnedLibrarian = findLibrarianById(library, parts[6]);
                            loan.returnBook(LocalDate.parse(parts[5]), returnedLibrarian);
                        }

                        loans.add(loan);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading loans: " + e.getMessage());
        }
        return loans;
    }

    private Book findBookByTitle(Library library, String title) {
        for (Book book : library.getBooks()) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    private Student findStudentById(Library library, String studentId) {
        for (Student student : library.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    private Librarian findLibrarianById(Library library, String librarianId) {
        for (Librarian librarian : library.getLibrarians()) {
            if (librarian.getEmployeeId().equals(librarianId)) {
                return librarian;
            }
        }
        return null;
    }
}