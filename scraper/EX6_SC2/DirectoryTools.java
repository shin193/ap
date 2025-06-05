package scraper.EX6_SC2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class DirectoryTools {
    public static boolean createDirectory(String directoryPath) {
        try {
            Files.createDirectories(Paths.get(directoryPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<String> getFilesAbsolutePathInDirectory(String directoryPath) {
        try {
            return Files.list(Paths.get(directoryPath))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

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


}
