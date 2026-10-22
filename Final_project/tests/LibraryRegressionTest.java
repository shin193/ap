package Final_project.tests;

import Final_project.*;
import java.util.*;

public class LibraryRegressionTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        Student owner = new Student("Owner", "s1", "owner", "pw");
        Student other = new Student("Other", "s2", "other", "pw");
        Database.saveStudents(Arrays.asList(owner, other));
        BookManager books = new BookManager(new Scanner("abc\n0\n-2\n7\n"));
        Book book = books.findBookById("0001");
        for (int i = 0; i < 3; i++) {
            books.requestBorrow(book.getId(), owner);
            check(book.isAvailable() && !book.isRequested(), "Invalid duration changed book");
        }
        books.receiveBook(book.getId());
        check(!book.isReceived(), "Available book marked received");
        books.requestBorrow(book.getId(), owner);
        books.receiveBook(book.getId());
        check(!book.isReceived(), "Unapproved request marked received");
        books.returnBook(book.getId());
        check(book.isRequested(), "Pending request returned as an active loan");
        books.approveBorrowRequest(book.getId());
        check(books.getBorrowedButNotReceivedBooks().contains(book), "Approved loan missing from collection list");
        StudentManager students = new StudentManager(new Scanner("4\nnew-id\n"));
        students.editStudentInformation(students.authenticateStudent("owner", "pw"));
        check("s1".equals(Database.loadStudents().get(0).getStudentId()), "ID edit orphaned active loan");
        students.registerStudent("Duplicate", "s1", "new-user", "pw");
        check(students.getStudentCount() == 2 && Database.loadStudents().size() == 2, "Duplicate ID accepted");
        books.receiveBook(book.getId());
        check(book.isReceived() && !books.getBorrowedButNotReceivedBooks().contains(book), "Collection state wrong");
        books.returnBook(book.getId(), other);
        check(!book.isAvailable(), "Another student returned owner's loan");
        books.returnBook(book.getId(), owner);
        check(books.getAvailableBooks().contains(book), "Returned book missing from available list");
        check(book.getReceivedDate() == null && book.getRequestDate() == null &&
                book.getBorrowDays() == 0 && book.getBorrowedByStudentId() == null, "Stale loan data after return");
        BookManager reopened = new BookManager(new Scanner(""));
        check(reopened.findBookById(book.getId()).isAvailable(), "Return lost after reload");
        System.out.println("PASS library-workflow");
    }
}
