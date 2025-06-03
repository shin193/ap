package scraper;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class DomainHtmlScraper {
    private String domainAddress;
    private Queue<String> queue;
    private Set<String> visitedUrls;
    private HtmlFileManager htmlFileManager;
    private ImageUrlManager imageUrlManager;

    public DomainHtmlScraper(String domainAddress, String savePath) {
        this.domainAddress = domainAddress;
        this.queue = new LinkedList<>();
        this.visitedUrls = new HashSet<>();
        this.htmlFileManager = new HtmlFileManager(savePath);
        this.imageUrlManager = new ImageUrlManager();
    }

    public void start() throws IOException {
        List<String> savedImageUrls = ImageUrlManager.loadImageUrls();
        if (savedImageUrls != null) {
            visitedUrls.addAll(savedImageUrls);
        }

        String startUrl = domainAddress;
        queue.add(startUrl);
        visitedUrls.add(startUrl);

        while (!queue.isEmpty()) {
            String url = queue.remove();
            try {
                System.out.println("Processing: " + url);
                List<String> htmlLines = HtmlFetcher.fetchHtml(url);
                this.htmlFileManager.save(htmlLines);

                List<String> imageUrls = HtmlParser.extractImageUrls(htmlLines);
                imageUrlManager.addImageUrls(imageUrls);

                List<String> urls = HtmlParser.getAllUrlsFromList(htmlLines)
                        .stream()
                        .filter(u -> u.startsWith(domainAddress))
                        .collect(Collectors.toList());

                for (String newUrl : urls) {
                    if (!visitedUrls.contains(newUrl)) {
                        visitedUrls.add(newUrl);
                        queue.add(newUrl);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing URL: " + url + " - " + e.getMessage());
            }
        }

        imageUrlManager.saveToFile();
        System.out.println("Scraping completed. Total URLs processed: " + visitedUrls.size());
    }
}
