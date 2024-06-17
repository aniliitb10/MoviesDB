package learning.streamlit.moviedb.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;


@Configuration(proxyBeanMethods = false)
public class SqliteConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix = "app.datasource")
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("app.datasource.driverClassName")));
        dataSource.setUrl(env.getProperty("app.datasource.jdbc-url"));
        dataSource.setUsername(env.getProperty("app.datasource.username"));
        dataSource.setPassword(env.getProperty("app.datasource.password"));
        return dataSource;
    }
}