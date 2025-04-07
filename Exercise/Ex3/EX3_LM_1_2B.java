package Exercise.Ex3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EX3_LM_1_2B {

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

        public void display() {
            System.out.println("Student: " + firstName + " " + lastName +
                    ", ID: " + studentID + ", Major: " + major);
        }
    }

    public static void main(String[] args) {
        Book[] books = new Book[4];
        Student[] students = new Student[3];

        try {
            BufferedReader bookReader = new BufferedReader(new FileReader("books.txt"));
            String line;
            int index = 0;
            while ((line = bookReader.readLine()) != null && index < books.length) {
                String[] parts = line.split(",");
                String name = parts[0];
                String author = parts[1];
                int pages = Integer.parseInt(parts[2]);
                int year = Integer.parseInt(parts[3]);
                books[index++] = new Book(name, author, pages, year);
            }
            bookReader.close();

            BufferedReader studentReader = new BufferedReader(new FileReader("students.txt"));
            index = 0;
            while ((line = studentReader.readLine()) != null && index < students.length) {
                String[] parts = line.split(",");
                String firstName = parts[0];
                String lastName = parts[1];
                String id = parts[2];
                String major = parts[3];
                students[index++] = new Student(firstName, lastName, id, major);
            }
            studentReader.close();

            System.out.println("Books read from file:");
            for (Book b : books) {
                b.display();
            }

            System.out.println("\nStudents read from file:");
            for (Student s : students) {
                s.display();
            }

        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
    }
}
