package Final_project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LibrarianManager {
    private List<Librarian> librarians = new ArrayList<>();
    private Scanner sc;

    public LibrarianManager() {
        this.librarians = FileHandling.loadLibrarians();
        if (librarians.isEmpty()) {
            addDefaultLibrarian();
        }
        sc = new Scanner(System.in);
    }


    private void addDefaultLibrarian() {
        Librarian defaultLibrarian = new Librarian("abc", "123456", "Default Librarian");
        librarians.add(defaultLibrarian);
        FileHandling.saveLibrarians(librarians);
    }

    private List<Librarian> getAllLibrarians() {
        return librarians;
    }

    public Librarian authenticateLibrarian(String username, String password) {
        return librarians.stream()
                .filter(l -> l.getUsername().equals(username) && l.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public void addLibrarian(String username, String password, String name) {
        if (isUsernameTaken(username)) {
            System.out.println("Username already taken!");
            return;
        }
        Librarian newLibrarian = new Librarian(username, password, name);
        librarians.add(newLibrarian);
        FileHandling.saveLibrarians(librarians);
        System.out.println("Librarian added successfully.");
    }

    private boolean isUsernameTaken(String username) {
        return librarians.stream().anyMatch(l -> l.getUsername().equals(username));
    }

    public void editLibrarianPassword(Librarian currentlibrarian) {
        System.out.println("Enter your new password: ");
        String newPassword = sc.nextLine();
        currentlibrarian.setPassword(newPassword);
        FileHandling.saveLibrarians(librarians);
        System.out.println("Your password has been changed.");
    }

    public List<Librarian> getLibrarians() {
        return librarians;
    }
}