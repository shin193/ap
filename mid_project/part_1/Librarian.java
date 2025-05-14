package mid_project.part_1;

public class Librarian {
    private String firstName;
    private String lastName;
    private String employeeId;

    public Librarian(String firstName, String lastName, String employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void changeInfo(String firstName, String lastName, String employeeId) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.employeeId=employeeId;
    }
    public void addBookToLibrary (Library library,String title,String author,int publishDate,int bookPage) {
        Book newBook = new Book(title, author, publishDate, bookPage);
        library.addBook(newBook);
        System.out.println("the book :"+newBook+" is added to the library");
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String toString() {
        return getFullName() + " (ID: " + employeeId + ")";
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

}
