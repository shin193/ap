package scraper.EX8;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class HtmlAnalyzer {
    private static List<String> fileList = DirectoryTools.getFilesAbsolutePathInDirectory(Conf.SAVE_DIRECTORY);

    public static List<String> getAllUrls() {
        return fileList.stream()
                .map(FileTools::getTextFileLines)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(HtmlParser::getFirstUrl)
                .filter(Objects::nonNull)
                .filter(s -> s.length() > 1)
                .collect(Collectors.toList());
    }

    public static void printTopCountUrls(int k) {
        ObjectCounter<String> urlCounter = new ObjectCounter<>();
        getAllUrls().forEach(urlCounter::add);
        for (Map.Entry<String, Integer> urlCountEntry : urlCounter.getTop(k)) {
            System.out.println(urlCountEntry.getKey() + " -> " + urlCountEntry.getValue());
        }
    }
}
