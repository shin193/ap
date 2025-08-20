package Final_project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandling {
    //student handling
    public static void saveDataSt(List<Student> students) {
        try (ObjectOutputStream oosStudents = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oosStudents.writeObject(students);
        } catch (IOException e) {
            System.out.println("an error occured while saving: " + e.getMessage());
        }
    }

    public static List<Student> loadDataSt() {
        File file = new File("students.dat");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream oisStudent = new ObjectInputStream(new FileInputStream("students.dat"))) {
            return (List<Student>) oisStudent.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("an error occured while loading: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    //book handling
    public static void saveDataBk(List<Book> books) {
        try (ObjectOutputStream oosBook = new ObjectOutputStream(new FileOutputStream("Books.dat"))) {
            oosBook.writeObject(books);
        } catch (IOException e) {
            System.out.println("an error occured while saving: " + e.getMessage());
        }
    }
    public static List<Book> loadDataBk() {
        File file = new File("Books.dat");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream oisBook = new ObjectInputStream(new FileInputStream("Books.dat"))) {
            return (List<Book>) oisBook.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("an error occured while loading: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}