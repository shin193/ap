package Final_project;

import java.io.Serializable;

public class Manager extends AbstractPerson implements Person,Serializable {
    private static final long serialVersionUID = 1L;

    public Manager(String username, String password, String name) {
        super(name,username,password);
    }
    @Override
    public String toString() {
        return "Manager: " + getName() + " (Username: " + getUsername() + ")";
    }
}