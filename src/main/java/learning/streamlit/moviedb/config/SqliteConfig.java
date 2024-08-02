package learning.streamlit.moviedb.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;


@Configuration(proxyBeanMethods = false)
@Profile("sqlite")
public class SqliteConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.hikari.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.datasource.hikari.jdbc-url"));
        dataSource.setUsername(env.getProperty("spring.datasource.hikari.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.hikari.password"));
        return dataSource;
    }
}