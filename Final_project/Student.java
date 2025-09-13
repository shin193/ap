package Final_project;

import java.io.Serializable;

public class Student extends AbstractPerson implements Serializable {
    private String studentId;
    private boolean Active;
    private static final long serialVersionUID = 1L;

    public Student(String name, String studentId, String username, String password) {
        super(name, username, password);
        this.studentId = studentId;
        this.Active = true;
    }
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public boolean isActive() {
        return Active;
    }
    public void setActive(boolean active) {
        Active = active;
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
                " | Student ID: " + studentId +
                " | Username: " + getUsername();
    }
}