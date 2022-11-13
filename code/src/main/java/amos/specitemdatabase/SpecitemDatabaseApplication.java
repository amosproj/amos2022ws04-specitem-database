package amos.specitemdatabase;

import amos.specitemdatabase.config.FileConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileConfig.class
})
public class SpecitemDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpecitemDatabaseApplication.class, args);
	}

}
