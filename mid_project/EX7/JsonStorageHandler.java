package mid_project.EX7;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonStorageHandler implements StorageHandler {
    private final Gson gson = new Gson();

    @Override
    public void saveLibrary(Library library) {
        try (FileWriter writer = new FileWriter("library.json")) {
            LibraryData data = new LibraryData(
                    library.getStudents(),
                    library.getBooks(),
                    library.getLibrarians(),
                    library.getLoans()
            );
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Error saving library: " + e.getMessage());
        }
    }

    @Override
    public Library loadLibrary() {
        try (FileReader reader = new FileReader("library.json")) {
            Type type = new TypeToken<LibraryData>(){}.getType();
            LibraryData data = gson.fromJson(reader, type);

            Manager manager = new Manager("arshia", "karimi", "12344321", "computer engineering");
            List<Librarian> librarians = data.getLibrarians();
            librarians.add(new Librarian("mohammad", "heydari", "0001"));
            librarians.add(new Librarian("jamal", "gondaviz", "0002"));

            Library library = new Library("University Library", manager, librarians);
            library.getStudents().addAll(data.getStudents());
            library.getBooks().addAll(data.getBooks());
            library.getLoans().addAll(data.getLoans());
            return library;
        } catch (IOException e) {
            System.out.println("Error loading library: " + e.getMessage());
            return createDefaultLibrary();
        }
    }

    private Library createDefaultLibrary() {
        Manager manager = new Manager("arshia", "karimi", "12344321", "computer engineering");
        List<Librarian> librarians = new ArrayList<>();
        librarians.add(new Librarian("mohammad", "heydari", "0001"));
        librarians.add(new Librarian("jamal", "gondaviz", "0002"));
        return new Library("University Library", manager, librarians);
    }

    private static class LibraryData {
        private final List<Student> students;
        private final List<Book> books;
        private final List<Librarian> librarians;
        private final List<Loan> loans;

        public LibraryData(List<Student> students, List<Book> books,
                           List<Librarian> librarians, List<Loan> loans) {
            this.students = students;
            this.books = books;
            this.librarians = librarians;
            this.loans = loans;
        }

        public List<Student> getStudents() { return students; }
        public List<Book> getBooks() { return books; }
        public List<Librarian> getLibrarians() { return librarians; }
        public List<Loan> getLoans() { return loans; }
    }
}