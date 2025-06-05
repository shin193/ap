package scraper.EX6_SC2;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

class HtmlFileManager {
    private String saveDirectory;

    public HtmlFileManager(String saveDirectory) {
        this.saveDirectory = saveDirectory;
        new File(saveDirectory).mkdirs();
    }

    public void save(List<String> lines) {
        try {
            String fileName = getFileName();
            PrintWriter out = new PrintWriter(new FileWriter(new File(saveDirectory, fileName)));
            for (String line : lines) {
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private String getFileName() {
        if (saveDirectory.endsWith(".html")) {
            return saveDirectory.substring(saveDirectory.lastIndexOf('/') + 1);
        }
        return "index.html";
    }
}
