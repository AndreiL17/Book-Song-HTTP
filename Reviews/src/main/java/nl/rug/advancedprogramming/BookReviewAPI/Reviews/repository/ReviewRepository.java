package nl.rug.advancedprogramming.BookReviewAPI.Reviews.repository;

import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ReviewRepository interface for performing CRUD operations on Review entities.
 * It extends the CrudRepository interface provided by Spring Data, offering generic methods
 * for interaction with the database, such as saving, deleting, and finding reviews.
 */
@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {

    /**
     * Finds all reviews for a given book based on the book's ID.
     *
     * @param bookId the ID of the book whose reviews are to be fetched
     * @return a list of reviews corresponding to the specified book ID
     */
    List<Review> findByBookId(int bookId);
}
