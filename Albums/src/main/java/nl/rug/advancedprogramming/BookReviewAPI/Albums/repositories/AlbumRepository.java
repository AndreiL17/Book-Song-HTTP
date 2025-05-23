package nl.rug.advancedprogramming.BookReviewAPI.Albums.repositories;

import nl.rug.advancedprogramming.BookReviewAPI.Albums.models.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Album entities, providing CRUD operations and custom query methods.
 */
@Repository
public interface AlbumRepository extends CrudRepository<Album, Integer> {

    /**
     * Retrieves all albums with the specified title.
     *
     * @param title the title of the albums to retrieve.
     * @return an iterable collection of albums with the specified title.
     */
    Iterable<Album> getByTitle(String title);

    /**
     * Retrieves all albums by the specified artist.
     *
     * @param artist the artist of the albums to retrieve.
     * @return an iterable collection of albums by the specified artist.
     */
    Iterable<Album> getByArtist(String artist);

    /**
     * Retrieves all albums with the specified genre.
     *
     * @param genre the genre of the albums to retrieve.
     * @return an iterable collection of albums with the specified genre.
     */
    Iterable<Album> getByGenre(String genre);
}
