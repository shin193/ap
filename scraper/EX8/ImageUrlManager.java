package scraper.EX8;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class ImageUrlManager {
    private static final String IMAGE_URLS_FILE = "image_urls.txt";
    private List<String> allImageUrls = new ArrayList<>();

    public void addImageUrls(List<String> urls) {
        allImageUrls.addAll(urls);
    }

    public void saveToFile() {
        try (FileWriter writer = new FileWriter(IMAGE_URLS_FILE)) {
            for (String url : allImageUrls) {
                writer.write(url + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error saving image URLs: " + e.getMessage());
        }
    }

    public static List<String> loadImageUrls() {
        try {
            return Files.readAllLines(Paths.get(IMAGE_URLS_FILE));
        } catch (IOException e) {
            return null;
        }
    }
}