/**
 * This package contains the repository classes for managing songs in the BookReview API.
 * It includes interfaces for database access and CRUD operations.
 */
package nl.rug.advancedprogramming.BookReviewAPI.Songs.repository;

import nl.rug.advancedprogramming.BookReviewAPI.Songs.models.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Song entities.
 * <p>
 * Provides methods for retrieving songs based on specific properties.
 * </p>
 */

@Repository
public interface SongRepository extends CrudRepository<Song, String> {

    /*
     The framework will automagically recognise these names and create the right queries in the implementation that it feeds the beans.
     See https://docs.spring.io/spring-data/data-jpa/docs/1.0.0.M1/reference/html/#jpa.query-methods.query-creation
     paragraph 2.1.2
     */

    /**
     * Retrieves books in database of given id.
     *
     * @param id id to query by.
     * @return {@link Iterable} of songs that match given id.
     */
    Iterable<Song> getById(int id);

    /**
     * Retrieves songs in database of given title.
     *
     * @param title Title to query by.
     * @return {@link Iterable} of songs that match given title.
     */
    Iterable<Song> getByTitle(String title);

    /**
     * Retrieves songs in database of given artist.
     *
     * @param artist Artist to query by.
     * @return {@link Iterable} of songs that match given artist.
     */
    Iterable<Song> getByArtist(String artist);

    /**
     * Retrieves songs in database of given label.
     *
     * @param label Label to query by.
     * @return {@link Iterable} of songs that match given label.
     */
    Iterable<Song> getByLabel(String label);

    /**
     * Retrieves songs in database of given genre.
     *
     * @param genre Genre to query by.
     * @return {@link Iterable} of songs that match given genre.
     */
    Iterable<Song> getByGenre(String genre);

    /**
     * Retrieves books in database of given length.
     *
     * @param length Length to query by.
     * @return {@link Iterable} of songs that match given title.
     */
    Iterable<Song> getByLength(int length);

}
