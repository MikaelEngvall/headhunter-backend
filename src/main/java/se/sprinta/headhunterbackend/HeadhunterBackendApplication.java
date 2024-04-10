package se.sprinta.headhunterbackend;

// Importing necessary libraries
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

// Log4j2 annotation for logging
@Log4j2
// Spring Boot Application annotation to mark this class as the main entry point of the application
@SpringBootApplication
public class HeadhunterBackendApplication {

    // Main method that starts the Spring Boot application
    public static void main(String[] args) {
        // SpringApplication.run method to bootstrap the application
        SpringApplication.run(HeadhunterBackendApplication.class, args);
    }

}