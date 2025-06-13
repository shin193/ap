package mid_project.EX7;

public class Manager {
    private final String firstName;
    private final String lastName;
    private final String education;
    private final String password;

    public Manager(String firstName, String lastName,String password,String education) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.education = education;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName() {

        return firstName + " " + lastName;
    }

    public String getEducation() {
            return education;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return "Manager: " + getFullName() + ", Education: " + getEducation();
    }
}
