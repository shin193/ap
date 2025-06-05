package scraper.EX6_SC2;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

class DomainHtmlScraper {
    private String domainAddress;
    private String domainBase;
    private Queue<String> queue;
    private Set<String> visitedUrls;
    private ImageUrlManager imageUrlManager;

    public DomainHtmlScraper(String domainAddress, String savePath) {
        this.domainAddress = domainAddress;
        try {
            URL url = new URL(domainAddress);
            this.domainBase = url.getHost();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid domain address: " + domainAddress);
        }

        this.queue = new LinkedList<>();
        this.visitedUrls = new HashSet<>();
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

                String savePath = getSavePath(url);
                new HtmlFileManager(savePath).save(htmlLines);

                List<String> imageUrls = HtmlParser.extractImageUrls(htmlLines);
                imageUrlManager.addImageUrls(imageUrls);

                List<String> urls = HtmlParser.getAllUrlsFromList(htmlLines)
                        .stream()
                        .filter(this::isSameDomain)
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

    private boolean isSameDomain(String url) {
        try {
            URL parsedUrl = new URL(url);
            String host = parsedUrl.getHost();

            return host.equals(domainBase) || host.endsWith("." + domainBase);
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private String getSavePath(String url) throws MalformedURLException {
        URL parsedUrl = new URL(url);
        String host = parsedUrl.getHost();
        String path = parsedUrl.getPath();

        String[] hostParts = host.split("\\.");
        StringBuilder pathBuilder = new StringBuilder(Conf.SAVE_DIRECTORY);

        if (hostParts.length > domainBase.split("\\.").length) {
            String subdomain = host.substring(0, host.indexOf(domainBase) - 1);
            pathBuilder.append("/_").append(subdomain.replace(".", "_"));
        }


        String[] pathParts = path.split("/");
        for (String part : pathParts) {
            if (!part.isEmpty() && !part.contains(".")) {
                pathBuilder.append("/").append(part);
            }
        }


        new File(pathBuilder.toString()).mkdirs();

        return pathBuilder.toString();
    }
}
