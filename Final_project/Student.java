package Final_project;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private String studentId;
    private String username;
    private String password;
    private boolean Active;
    private static final long serialVersionUID = 1L;

    public Student(String name, String studentId, String username, String password) {
        this.name = name;
        this.studentId = studentId;
        this.username = username;
        this.password = password;
        this.Active = true;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return Active;
    }
    public void setActive(boolean active) {
        Active = active;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                " | Student ID: " + studentId +
                " | Username: " + username +
                " | Account Status : " + isActive();
    }
}