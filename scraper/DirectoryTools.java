package scraper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
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
}
