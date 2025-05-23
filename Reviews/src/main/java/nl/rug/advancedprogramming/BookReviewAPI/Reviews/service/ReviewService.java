package nl.rug.advancedprogramming.BookReviewAPI.Reviews.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.Review;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling the business logic related to Reviews.
 * It interacts with the ReviewRepository to perform CRUD operations and handles import/export functionality.
 */
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    /**
     * Constructor for the ReviewService.
     *
     * @param reviewRepository the repository for accessing review data
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Adds a new review to the repository.
     *
     * @param review the review to be added
     * @return a ResponseEntity with the appropriate HTTP status
     */
    public ResponseEntity<Review> addReview(Review review) {
        reviewRepository.save(review);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Fetches a review by its ID.
     *
     * @param id the ID of the review
     * @return an Optional containing the review if found, or empty if not
     */
    public Optional<Review> getReview(int id) {
        return reviewRepository.findById(id);
    }

    /**
     * Updates an existing review by its ID with new data.
     *
     * @param reviewId      the ID of the review to update
     * @param updatedReview the new review data
     * @return true if the review was successfully updated, false otherwise
     */
    public boolean updateReview(int reviewId, Review updatedReview) {
        Optional<Review> toBeUpdated = reviewRepository.findById(reviewId);
        if (toBeUpdated.isPresent()) {
            Review existingReview = toBeUpdated.get();
            existingReview.setReviewId(updatedReview.getReviewId());
            existingReview.setBookId(updatedReview.getBookId());
            existingReview.setRating(updatedReview.getRating());
            existingReview.setComment(updatedReview.getComment());
            reviewRepository.save(existingReview);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a review by its ID.
     *
     * @param reviewId the ID of the review to delete
     * @return true if the review was deleted, false otherwise
     */
    public boolean deleteReview(int reviewId) {
        Optional<Review> toBeDeleted = reviewRepository.findById(reviewId);
        if (toBeDeleted.isPresent()) {
            reviewRepository.deleteById(reviewId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves all reviews, optionally filtered by book ID.
     *
     * @param bookId the ID of the book to filter reviews by, or 0 for all reviews
     * @return a list of reviews
     */
    public List<Review> getAllReviews(int bookId) {
        if (bookId < 1) {
            return (List<Review>) reviewRepository.findAll();
        }
        return reviewRepository.findByBookId(bookId);
    }

    /**
     * Calculates the average rating for a book by its ID.
     *
     * @param bookId the ID of the book to calculate the average rating for
     * @return the average rating of the book
     */
    public float calculateAverageRating(int bookId) {
        List<Review> reviews = getAllReviews(bookId);
        float sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }

    /**
     * Imports reviews from a JSON string and saves them in the repository.
     *
     * @param data the JSON string containing the review data
     * @throws IOException if an error occurs during parsing
     */
    public void importReviewsJSON(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Review> reviews;
        reviews = objectMapper.readValue(data, new TypeReference<ArrayList<Review>>() {
        });
        reviewRepository.saveAll(reviews);
    }

    /**
     * Imports reviews from a CSV string and saves them in the repository.
     *
     * @param data the CSV string containing the review data
     * @throws IOException if an error occurs during parsing
     */
    public void importReviewsCSV(String data) throws IOException {
        List<Review> reviews = new ArrayList<>();
        String[] lines = data.split("\\r?\\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            int bookId = Integer.parseInt(fields[0].trim());
            float rating = Float.parseFloat(fields[1].trim());
            String comment = fields[2].replace("\"", "").trim();
            String reviewDateStr = fields[3].trim();
            java.util.Date reviewDate;
            try {
                reviewDate = dateFormat.parse(reviewDateStr);
                Review review = new Review(bookId, rating, comment, reviewDate);
                reviews.add(review);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        reviewRepository.saveAll(reviews);
    }

    /**
     * Exports reviews in JSON format for a specific book.
     *
     * @param bookId the ID of the book whose reviews are to be exported
     * @return a JSON string representing the reviews
     */
    public String exportReviewsJSON(int bookId) {
        List<Review> toBeExported = getAllReviews(bookId);
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (Review review : toBeExported) {
            sb.append(review.toJSON());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Exports reviews in CSV format for a specific book.
     *
     * @param bookId the ID of the book whose reviews are to be exported
     * @return a CSV string representing the reviews
     */
    public String exportReviewsCSV(int bookId) {
        List<Review> toBeExported = getAllReviews(bookId);
        StringBuilder sb = new StringBuilder();
        sb.append("reviewId,bookId,rating,comment,reviewDate\n");
        for (Review review : toBeExported) {
            sb.append(review.toCSV());
        }
        return sb.toString();
    }
}
