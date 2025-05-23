package nl.rug.advancedprogramming.BookReviewAPI.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {
		"nl.rug.advancedprogramming.BookReviewAPI.Books",
		"nl.rug.advancedprogramming.BookReviewAPI.Reviews",
		"nl.rug.advancedprogramming.BookReviewAPI.Songs",
		"nl.rug.advancedprogramming.BookReviewAPI.Application.configuration",
		"nl.rug.advancedprogramming.BookReviewAPI.Albums",
})
@EnableJpaRepositories(basePackages = {
		"nl.rug.advancedprogramming.BookReviewAPI.Books.*",
		"nl.rug.advancedprogramming.BookReviewAPI.Reviews.*",
		"nl.rug.advancedprogramming.BookReviewAPI.Songs",
		"nl.rug.advancedprogramming.BookReviewAPI.Application.configuration",
		"nl.rug.advancedprogramming.BookReviewAPI.Albums.*"
})
@EntityScan(basePackages = {
		"nl.rug.advancedprogramming.BookReviewAPI.Books.*",
		"nl.rug.advancedprogramming.BookReviewAPI.Reviews.*",
		"nl.rug.advancedprogramming.BookReviewAPI.Songs",
		"nl.rug.advancedprogramming.BookReviewAPI.Application.configuration",
		"nl.rug.advancedprogramming.BookReviewAPI.Albums.*"
})

@SpringBootApplication()
public class BookReviewApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookReviewApiApplication.class, args);
	}

}
