package Exercise.Ex3;

import java.io.*;

public class EX3_LM_1_2C {

    public static class Book {
        String bookName;
        String authorName;
        int pageCount;
        int yearOfPublication;

        public Book(String bookName, String authorName, int pageCount, int yearOfPublication) {
            this.bookName = bookName;
            this.authorName = authorName;
            this.pageCount = pageCount;
            this.yearOfPublication = yearOfPublication;
        }

        public String toFileString() {
            return bookName + "," + authorName + "," + pageCount + "," + yearOfPublication;
        }

        public void display() {
            System.out.println("Book: " + bookName + ", Author: " + authorName +
                    ", Pages: " + pageCount + ", Year: " + yearOfPublication);
        }
    }

    public static class Student {
        String firstName;
        String lastName;
        String studentID;
        String major;

        public Student(String firstName, String lastName, String studentID, String major) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.studentID = studentID;
            this.major = major;
        }

        public String toFileString() {
            return firstName + "," + lastName + "," + studentID + "," + major;
        }

        public void display() {
            System.out.println("Student: " + firstName + " " + lastName +
                    ", ID: " + studentID + ", Major: " + major);
        }
    }
    public static void saveBooksToFile(Book[] books, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (Book b : books) {
                writer.write(b.toFileString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing books: " + e.getMessage());
        }
    }

    public static void saveStudentsToFile(Student[] students, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (Student s : students) {
                writer.write(s.toFileString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing students: " + e.getMessage());
        }
    }

    public static Book[] readBooksFromFile(String filename, int size) {
        Book[] books = new Book[size];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < size) {
                String[] parts = line.split(",");
                books[index++] = new Book(parts[0], parts[1],
                        Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
        return books;
    }

    public static Student[] readStudentsFromFile(String filename, int size) {
        Student[] students = new Student[size];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < size) {
                String[] parts = line.split(",");
                students[index++] = new Student(parts[0], parts[1], parts[2], parts[3]);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading students: " + e.getMessage());
        }
        return students;
    }

    public static void main(String[] args) {
        Book[] books = {
                new Book("book 1", "author1", 800, 2001),
                new Book("book 2", "author2", 100, 2023),
                new Book("book 3", "author3", 760, 2025),
                new Book("book 4", "author4", 940, 1992)
        };

        Student[] students = {
                new Student("Ali", "Rezaei", "403001253", "Computer Engineering"),
                new Student("Sara", "Ahmadi", "402502225", "Electrical Engineering"),
                new Student("Reza", "bagheri", "401403862", "mechanic")
        };

        saveBooksToFile(books, "books.txt");
        saveStudentsToFile(students, "students.txt");

        Book[] loadedBooks = readBooksFromFile("books.txt", 4);
        Student[] loadedStudents = readStudentsFromFile("students.txt", 3);

        System.out.println("Loaded books:");
        for (Book b : loadedBooks) {
            b.display();
        }

        System.out.println("\nLoaded students:");
        for (Student s : loadedStudents) {
            s.display();
        }
    }
}
