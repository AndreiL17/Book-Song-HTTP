package nl.rug.advancedprogramming.BookReviewAPI.Application.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConnection {

    @Bean
    public static DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
                .driverClassName("org.mariadb.jdbc.Driver")
                .url("jdbc:mariadb://"
                        + System.getenv("DB_HOST")
                        + ":" + System.getenv("DB_PORT")
                        + "/"
                        + System.getenv("DB_DATABASE")
                )
                .username(System.getenv("DB_USER"))
                .password(System.getenv("DB_PASSWORD"));

        return dataSourceBuilder.build();
    }
}
