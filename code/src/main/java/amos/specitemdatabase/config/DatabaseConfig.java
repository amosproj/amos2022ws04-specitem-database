package amos.specitemdatabase.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration for the database.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("amos.specitemdatabase.repo")
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
}
