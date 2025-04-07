package Exercise.Ex3;

import java.io.FileWriter;
import java.io.IOException;

public class EX3_LM_1_2A {

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

        try {
            FileWriter bookWriter = new FileWriter("books.txt");
            for (Book b : books) {
                bookWriter.write(b.toFileString() + "\n");
            }
            bookWriter.close();

            FileWriter studentWriter = new FileWriter("students.txt");
            for (Student s : students) {
                studentWriter.write(s.toFileString() + "\n");
            }
            studentWriter.close();

            System.out.println("Data successfully saved to files.");
        } catch (IOException e) {
            System.out.println("Error writing to files: " + e.getMessage());
        }
    }
}
