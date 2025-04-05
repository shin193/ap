package Exercise.Ex3;

public class EX3_LM_1_1 {

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

        public void displayBookInfo() {
            System.out.println("Book Name: " + bookName);
            System.out.println("Author Name: " + authorName);
            System.out.println("Page Count: " + pageCount);
            System.out.println("Year of Publication: " + yearOfPublication);
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

        public void displayStudentInfo() {
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Student ID: " + studentID);
            System.out.println("Major: " + major);
        }
    }

    public static void main(String[] args) {
        Book book1 = new Book("book number 1", "author number 2", 200, 2025);
        Book book2 = new Book("book number 2", "author number 2", 800, 2019);

        Student student1 = new Student("Ali", "bagheri", "40456787", "mechanic");
        Student student2 = new Student("mohammad", "mohammadi", "40244442", "electronic Engineering");

        System.out.println("Book 1 Information:");
        book1.displayBookInfo();
        System.out.println("\nBook 2 Information:");
        book2.displayBookInfo();

        System.out.println("\nStudent 1 Information:");
        student1.displayStudentInfo();
        System.out.println("\nStudent 2 Information:");
        student2.displayStudentInfo();
    }
}
