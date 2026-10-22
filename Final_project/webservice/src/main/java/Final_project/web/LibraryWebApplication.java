package Final_project.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class LibraryWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryWebApplication.class, args);
    }
}
