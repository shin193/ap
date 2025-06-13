package scraper.EX8;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DomainHtmlScraper {
    private final String domainAddress;
    private final String domainBase;
    private final Queue<String> urlQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final ImageUrlManager imageUrlManager = new ImageUrlManager();
    private final SongUrlManager songUrlManager = new SongUrlManager();
    private final ExecutorService executorService;
    private final AtomicInteger processedCount = new AtomicInteger(0);
    private final Object lock = new Object();

    public DomainHtmlScraper(String domainAddress) {
        this.domainAddress = domainAddress;
        try {
            URL url = new URL(domainAddress);
            this.domainBase = url.getHost();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid domain address: " + domainAddress);
        }
        this.executorService = initializeThreadPool();
    }

    private ExecutorService initializeThreadPool() {
        return Conf.THREAD_COUNT > 0
                ? Executors.newFixedThreadPool(Conf.THREAD_COUNT)
                : Executors.newSingleThreadExecutor();
    }

    public void start() throws IOException, InterruptedException {
        try {
            createDirectories();
            initializeStartingUrl();
            processUrls();
        } finally {
            shutdownAndAwaitTermination();
            saveCollectedUrls();
            printCompletionMessage();
        }
    }

    private void processUrls() throws InterruptedException {
        List<Future<?>> futures = new ArrayList<>();

        while (!urlQueue.isEmpty() || hasActiveTasks(futures)) {
            if (!urlQueue.isEmpty() && processedCount.get() < Conf.MAX_PAGES) {
                String url = urlQueue.poll();
                futures.add(executorService.submit(() -> processUrl(url)));

                if (Conf.THREAD_COUNT == 0) {
                    waitForSingleThread(futures);
                }
            }
            cleanCompletedFutures(futures);
            Thread.sleep(100);
        }
    }

    private void waitForSingleThread(List<Future<?>> futures) throws InterruptedException {
        try {
            futures.get(0).get();
            futures.clear();
        } catch (ExecutionException e) {
            System.err.println("Error in single thread execution: " + e.getCause().getMessage());
            futures.clear();
        }
    }

    private void processUrl(String url) {
        try {
            processedCount.incrementAndGet();
            logProcessingStart(url);

            List<String> htmlLines = HtmlFetcher.fetchHtml(url);
            saveHtmlContent(url, htmlLines);
            processResources(htmlLines);
            processNewUrls(htmlLines);

            if (Conf.THREAD_COUNT == 0) {
                Thread.sleep(Conf.DOWNLOAD_DELAY_SECONDS * 1000);
            }
        } catch (Exception e) {
            handleProcessingError(url, e);
        }
    }

    private void cleanCompletedFutures(List<Future<?>> futures) {
        Iterator<Future<?>> iterator = futures.iterator();
        while (iterator.hasNext()) {
            Future<?> future = iterator.next();
            if (future.isDone()) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    System.err.println("Error in thread execution: " + e.getCause().getMessage());
                }
                iterator.remove();
            }
        }
    }

    private boolean hasActiveTasks(List<Future<?>> futures) {
        return !futures.isEmpty();
    }

    private void createDirectories() throws IOException {
        Files.createDirectories(Paths.get(Conf.SAVE_DIRECTORY));
        Files.createDirectories(Paths.get(Conf.IMAGE_DIRECTORY));
        Files.createDirectories(Paths.get(Conf.SONG_DIRECTORY));
    }

    private void initializeStartingUrl() {
        urlQueue.add(domainAddress);
        visitedUrls.add(domainAddress);
    }

    private void logProcessingStart(String url) {
        System.out.printf("[%s] Processing: %s (%d/%d)%n",
                Thread.currentThread().getName(), url,
                processedCount.get(), Conf.MAX_PAGES);
    }

    private void saveHtmlContent(String url, List<String> htmlLines) throws IOException {
        String savePath = getSavePath(url);
        new HtmlFileManager(savePath).save(htmlLines);
    }

    private void processResources(List<String> htmlLines) {
        processImages(htmlLines);
        processSongs(htmlLines);
    }

    private void processImages(List<String> htmlLines) {
        List<String> imageUrls = HtmlParser.extractUrlsWithExtensions(
                htmlLines, Arrays.asList("jpg", "jpeg", "png", "gif"));
        downloadResources(imageUrls, Conf.IMAGE_DIRECTORY);
        imageUrlManager.addImageUrls(imageUrls);
    }

    private void processSongs(List<String> htmlLines) {
        List<String> songUrls = HtmlParser.extractUrlsWithExtensions(
                htmlLines, Arrays.asList("mp3", "wav", "ogg"));
        downloadResources(songUrls, Conf.SONG_DIRECTORY);
        songUrlManager.addSongUrls(songUrls);
    }

    private void processNewUrls(List<String> htmlLines) {
        List<String> newUrls = HtmlParser.getAllUrlsFromList(htmlLines)
                .stream()
                .filter(this::isSameDomain)
                .filter(url -> !visitedUrls.contains(url))
                .collect(Collectors.toList());

        synchronized (lock) {
            newUrls.forEach(url -> {
                visitedUrls.add(url);
                urlQueue.add(url);
            });
        }
    }

    private void downloadResources(List<String> urls, String targetDirectory) {
        urls.parallelStream().forEach(url -> {
            try {
                String fileName = getFileNameFromUrl(url);
                Path targetPath = Paths.get(targetDirectory, fileName);

                if (!Files.exists(targetPath)) {
                    try (InputStream in = new URL(url).openStream()) {
                        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        logDownloadSuccess(fileName);
                    }
                }
            } catch (IOException e) {
                logDownloadFailure(url, e);
            }
        });
    }

    private String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void logDownloadSuccess(String fileName) {
        System.out.printf("[%s] Downloaded: %s%n",
                Thread.currentThread().getName(), fileName);
    }

    private void logDownloadFailure(String url, Exception e) {
        System.err.printf("[%s] Failed to download %s: %s%n",
                Thread.currentThread().getName(), url, e.getMessage());
    }

    private void handleProcessingError(String url, Exception e) {
        System.err.printf("[%s] Error processing URL %s: %s%n",
                Thread.currentThread().getName(), url, e.getMessage());
    }

    private void shutdownAndAwaitTermination() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void saveCollectedUrls() {
        imageUrlManager.saveToFile();
        songUrlManager.saveToFile();
    }

    private void printCompletionMessage() {
        System.out.printf("%nScraping completed!%n");
        System.out.printf("- Total URLs processed: %d%n", processedCount.get());
        System.out.printf("- Unique pages visited: %d%n", visitedUrls.size());
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

        StringBuilder pathBuilder = new StringBuilder(Conf.SAVE_DIRECTORY);

        if (!host.equals(domainBase)) {
            pathBuilder.append("/_").append(host.replace(".", "_"));
        }

        Arrays.stream(path.split("/"))
                .filter(part -> !part.isEmpty() && !part.contains("."))
                .forEach(part -> pathBuilder.append("/").append(part));

        new File(pathBuilder.toString()).mkdirs();
        return pathBuilder.toString();
    }
}