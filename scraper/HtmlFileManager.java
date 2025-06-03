package scraper;
import java.io.PrintWriter;
import java.util.List;

class HtmlFileManager {
    private String saveFileBasePath;
    private static int saveCounter = 0;

    public HtmlFileManager(String saveFileBasePath) {
        this.saveFileBasePath = saveFileBasePath;
        DirectoryTools.createDirectory(saveFileBasePath);
    }

    public void save(List<String> lines) {
        try {
            String saveHtmlFileAddress = getSaveHtmlFileAddress();
            PrintWriter out = new PrintWriter(saveHtmlFileAddress);
            for (String line : lines) {
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public String getSaveHtmlFileAddress() {
        saveCounter++;
        return saveFileBasePath + "/" + saveCounter + ".html";
    }
}
