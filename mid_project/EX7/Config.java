package mid_project.EX7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    private static String storageType = "tabsplit";

    public static void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("storage=")) {
                    storageType = line.substring(8).trim().toLowerCase();
                }
            }
        } catch (IOException e) {
            System.out.println("Using default tab-split storage (config.txt not found)");
        }
    }

    public static String getStorageType() {
        return storageType;
    }
}