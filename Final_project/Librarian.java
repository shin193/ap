package Final_project;

import java.io.Serializable;

public class Librarian extends AbstractPerson implements Person,Serializable {
    private static final long serialVersionUID = 1L;

    public Librarian(String username, String password, String name) {
        super(name,username,password);
    }
    @Override
    public String toString() {
        return "Librarian: " + getName() + " (Username: " + getUsername() + ")";
    }
}