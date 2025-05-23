package nl.rug.advancedprogramming.BookReviewAPI.Reviews.repository;

import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.SongReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing `SongReview` entities, providing CRUD operations and
 * additional query methods for song reviews.
 */
@Repository
public interface SongReviewRepository extends CrudRepository<SongReview, Integer> {

    /**
     * Retrieves a list of song reviews based on the ID of the associated song.
     *
     * @param songId the ID of the song whose reviews are to be retrieved.
     * @return a list of `SongReview` objects for the specified song.
     */
    List<SongReview> findBySongId(int songId);
}
