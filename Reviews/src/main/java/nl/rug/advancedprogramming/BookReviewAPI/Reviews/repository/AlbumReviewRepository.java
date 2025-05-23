package nl.rug.advancedprogramming.BookReviewAPI.Reviews.repository;

import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.AlbumReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing `AlbumReview` entities, providing CRUD operations and
 * additional query methods for album reviews.
 */
@Repository
public interface AlbumReviewRepository extends CrudRepository<AlbumReview, Integer> {

    /**
     * Retrieves a list of album reviews based on the ID of the associated album.
     *
     * @param albumId the ID of the album whose reviews are to be retrieved.
     * @return a list of `AlbumReview` objects for the specified album.
     */
    List<AlbumReview> findByAlbumId(int albumId);
}
