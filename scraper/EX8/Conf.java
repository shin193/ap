package scraper.EX8;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class Conf {
    public static String DOMAIN_ADDRESS = "https://znu.ac.ir";
    public static String SAVE_DIRECTORY = "fetched_html";
    public static String IMAGE_DIRECTORY = "images";
    public static String SONG_DIRECTORY = "songs";
    public static int DOWNLOAD_DELAY_SECONDS = 2;
    public static int THREAD_COUNT = 0;
    public static int MAX_PAGES = 1000;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            THREAD_COUNT = Integer.parseInt(prop.getProperty("thread-count", "0"));

            DOMAIN_ADDRESS = prop.getProperty("domain-address", DOMAIN_ADDRESS);
            SAVE_DIRECTORY = prop.getProperty("save-directory", SAVE_DIRECTORY);
            IMAGE_DIRECTORY = prop.getProperty("image-directory", IMAGE_DIRECTORY);
            SONG_DIRECTORY = prop.getProperty("song-directory", SONG_DIRECTORY);
            DOWNLOAD_DELAY_SECONDS = Integer.parseInt(prop.getProperty("download-delay", "2"));
            MAX_PAGES = Integer.parseInt(prop.getProperty("max-pages", "1000"));
        } catch (IOException e) {
            System.err.println("Config file not found, using default settings");
        }
    }
}