package scraper.EX8;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class HtmlParser {
    public static String getFirstUrl(String htmlLine) {
        String url = null;
        int startIndex = htmlLine.indexOf("href=\"");
        if (startIndex >= 0) {
            try {
                int hrefLength = "href\"".length();
                int endIndex = htmlLine.indexOf("\"", startIndex + hrefLength + 1);
                url = htmlLine.substring(startIndex + hrefLength + 1, endIndex);
            } catch (Exception e) {}
        }
        return url;
    }

    public static List<String> extractUrlsWithExtensions(List<String> htmlLines, List<String> extensions) {
        return htmlLines.stream()
                .map(line -> {
                    String url = null;
                    int startIndex = line.indexOf("href=\"");
                    if (startIndex < 0) startIndex = line.indexOf("src=\"");
                    if (startIndex >= 0) {
                        try {
                            int endIndex = line.indexOf("\"", startIndex + 6);
                            url = line.substring(startIndex + 6, endIndex);
                            if (url.startsWith("/")) {
                                url = Conf.DOMAIN_ADDRESS + url;
                            }
                            String finalUrl = url.toLowerCase();
                            if (extensions.stream().anyMatch(ext -> finalUrl.endsWith("." + ext))) {
                                return url;
                            }
                        } catch (Exception ignored) {}
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static List<String> getAllUrlsFromList(List<String> htmlLines) {
        return htmlLines.stream()
                .map(HtmlParser::getFirstUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}