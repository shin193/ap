package Final_project;

import java.util.Scanner;

public class ManagerManager {
    private Manager manager;
    private Scanner sc;

    public ManagerManager() {
        this(new Scanner(System.in));
    }

    public ManagerManager(Scanner sc) {
        this.manager = Database.loadManager();
        if (manager == null) {
            createDefaultManager();
        }
        this.sc = sc;
    }

    private void createDefaultManager() {
        manager = new Manager("ADMIN", Passwords.hash("1111"), "System Administrator");
        Database.saveManager(manager);
    }

    public Manager authenticateManager(String username, String password) {
        if (manager != null && manager.getUsername().equals(username) && Passwords.matches(password, manager.getPassword())) {
            return manager;
        }
        return null;
    }

    public void editManagerPassword(Manager currentManager) {
        System.out.println("Enter your new password: ");
        String newPassword = sc.nextLine();
        currentManager.setPassword(Passwords.hash(newPassword));
        Database.saveManager(currentManager);
        System.out.println("Your password has been changed.");
    }

    public Manager getManager() {
        return manager;
    }
}
