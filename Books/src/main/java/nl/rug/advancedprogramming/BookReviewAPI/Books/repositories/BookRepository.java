package nl.rug.advancedprogramming.BookReviewAPI.Books.repositories;

import nl.rug.advancedprogramming.BookReviewAPI.Books.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {

    // The framework will automagically recognise these names and create the right queries in the implementation that it feeds the beans.
    // See https://docs.spring.io/spring-data/data-jpa/docs/1.0.0.M1/reference/html/#jpa.query-methods.query-creation
    // paragraph 2.1.2

    /**
     * Retrieves books in database of given title.
     *
     * @param title Title to query by.
     * @return {@link Iterable} of books that match given title.
     */
    Iterable<Book> getByTitle(String title);

    /**
     * Retrieves books in database of given author.
     *
     * @param author Author to query by.
     * @return {@link Iterable} of books that match given title.
     */
    Iterable<Book> getByAuthor(String author);

    /**
     * Retrieves books in database of given publisher.
     *
     * @param publisher Publisher to query by.
     * @return {@link Iterable} of books that match given title.
     */
    Iterable<Book> getByPublisher(String publisher);

    /**
     * Retrieves books in database of given ISBN.
     *
     * @param isbn ISBN to query by.
     * @return {@link Iterable} of books that match given title.
     */
    Iterable<Book> getByIsbn(String isbn);

    /**
     * Retrieves books in database of given genre.
     *
     * @param genre Genre to query by.
     * @return {@link Iterable} of books that match given title.
     */
    Iterable<Book> getByGenre(String genre);

    /**
     * Retrieves books in database of given price.
     *
     * @param price Price to query by. Must be an exact match.
     * @return {@link Iterable} of books that match given title.
     */
    Iterable<Book> getByPrice(double price);
}
