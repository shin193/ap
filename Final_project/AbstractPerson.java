package Final_project;

import java.io.Serializable;

public abstract class AbstractPerson implements Person, Serializable {
    protected String name;
    protected String username;
    protected String password;
    private static final long serialVersionUID = 1L;

    public AbstractPerson(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}