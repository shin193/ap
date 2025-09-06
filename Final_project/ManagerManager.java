package Final_project;

import java.util.Scanner;

public class ManagerManager {
    private Manager manager;
    private Scanner sc;

    public ManagerManager() {
        this.manager = FileHandling.loadManager();
        if (manager == null) {
            createDefaultManager();
        }
        sc = new Scanner(System.in);
    }

    private void createDefaultManager() {
        manager = new Manager("ADMIN", "1111", "System Administrator");
        FileHandling.saveManager(manager);
    }

    public Manager authenticateManager(String username, String password) {
        if (manager != null && manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
            return manager;
        }
        return null;
    }

    public void editManagerPassword(Manager currentManager) {
        System.out.println("Enter your new password: ");
        String newPassword = sc.nextLine();
        currentManager.setPassword(newPassword);
        FileHandling.saveManager(currentManager);
        System.out.println("Your password has been changed.");
    }

    public Manager getManager() {
        return manager;
    }
}