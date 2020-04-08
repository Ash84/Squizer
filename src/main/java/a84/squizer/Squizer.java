package a84.squizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Squizer {

	public static void main(String[] args) {
		System.setProperty("server.port", "8085");
		SpringApplication.run(Squizer.class, args);
	}

}
