package mid_project.EX7;

import java.time.LocalDate;

public class Student {
    private final String firstName;
    private final String lastName;
    private final String studentId;
    private final String major;
    private final LocalDate membershipDate;
    public Student(String firstName, String lastName, String studentId, String major, LocalDate membershipDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.major = major;
        this.membershipDate = membershipDate;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getStudentId(){
        return studentId;
    }
    public String toString() {
        return firstName + " " + lastName+" "+studentId+" "+major+" "+membershipDate;
    }

    public LocalDate getRegisterDate() {
        return membershipDate;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getMajor() {
        return major;
    }

}