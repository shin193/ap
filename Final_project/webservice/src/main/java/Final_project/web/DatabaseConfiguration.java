package Final_project.web;

import Final_project.Database;
import Final_project.LibrarianManager;
import Final_project.ManagerManager;
import java.nio.file.Path;
import java.util.Scanner;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

@Configuration
public class DatabaseConfiguration {
    @Bean
    DataSource dataSource(@Value("${library.database.path}") String databasePath) {
        String path = Path.of(databasePath).toAbsolutePath().normalize().toString();
        System.setProperty("library.db.path", path);
        // Reuse the existing schema, legacy migration and initial staff accounts.
        Database.loadStudents();
        new ManagerManager(new Scanner(""));
        new LibrarianManager(new Scanner(""));
        SQLiteConfig config = new SQLiteConfig();
        config.setBusyTimeout(5000);
        config.enforceForeignKeys(true);
        SQLiteDataSource source = new SQLiteDataSource(config);
        source.setUrl("jdbc:sqlite:" + path);
        return source;
    }
}
