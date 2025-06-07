package scraper.EX6_SC3;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

class DomainHtmlScraper {
    private String domainAddress;
    private String domainBase;
    private Queue<String> queue;
    private Set<String> visitedUrls;
    private ImageUrlManager imageUrlManager;
    private SongUrlManager songUrlManager;

    public DomainHtmlScraper(String domainAddress) {
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
        this.songUrlManager = new SongUrlManager();
    }

    public void start() throws IOException, InterruptedException {
        DirectoryTools.createDirectory(Conf.SAVE_DIRECTORY);
        DirectoryTools.createDirectory(Conf.IMAGE_DIRECTORY);
        DirectoryTools.createDirectory(Conf.SONG_DIRECTORY);

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

                List<String> imageUrls = HtmlParser.extractUrlsWithExtensions(htmlLines, Arrays.asList("jpg", "jpeg", "png", "gif"));
                downloadResources(imageUrls, Conf.IMAGE_DIRECTORY);

                List<String> songUrls = HtmlParser.extractUrlsWithExtensions(htmlLines, Arrays.asList("mp3", "wav", "ogg"));
                downloadResources(songUrls, Conf.SONG_DIRECTORY);

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

                Thread.sleep(Conf.DOWNLOAD_DELAY_SECONDS * 1000);
            } catch (Exception e) {
                System.err.println("Error processing URL: " + url + " - " + e.getMessage());
            }
        }
        System.out.println("Scraping completed. Total URLs processed: " + visitedUrls.size());
    }

    private void downloadResources(List<String> urls, String targetDirectory) {
        for (String url : urls) {
            try {
                String fileName = url.substring(url.lastIndexOf('/') + 1);
                String filePath = targetDirectory + "/" + fileName;
                InputStream in = new URL(url).openStream();
                Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded: " + fileName);
            } catch (Exception e) {
                System.err.println("Failed to download: " + url);
            }
        }
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