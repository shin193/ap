package scraper.EX8;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class SongUrlManager {
    private static final String SONG_URLS_FILE = "song_urls.txt";
    private List<String> allSongUrls = new ArrayList<>();

    public void addSongUrls(List<String> urls) {
        allSongUrls.addAll(urls);
    }

    public void saveToFile() {
        try (FileWriter writer = new FileWriter(SONG_URLS_FILE)) {
            for (String url : allSongUrls) {
                writer.write(url + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error saving song URLs: " + e.getMessage());
        }
    }

    public static List<String> loadSongUrls() {
        try {
            return Files.readAllLines(Paths.get(SONG_URLS_FILE));
        } catch (IOException e) {
            return null;
        }
    }
}