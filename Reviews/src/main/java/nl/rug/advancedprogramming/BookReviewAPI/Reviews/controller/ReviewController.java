package nl.rug.advancedprogramming.BookReviewAPI.Reviews.controller;

import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.Review;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for handling HTTP requests related to reviews.
 * Provides CRUD operations for reviews, as well as import/export functionalities
 * in JSON and CSV formats.
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Constructor to initialize the ReviewService.
     *
     * @param reviewService Service layer for review operations.
     */
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Adds a new review.
     *
     * @param review Review to be added.
     * @return ResponseEntity with the created review and HTTP status code.
     */
    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id ID of the review to be retrieved.
     * @return ResponseEntity containing the review if found, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable int id) {
        Optional<Review> review = reviewService.getReview(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing review.
     *
     * @param id            ID of the review to be updated.
     * @param updatedReview Updated review details.
     * @return ResponseEntity with HTTP status 200 if successful, or 404 if the review is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable int id, @RequestBody Review updatedReview) {
        boolean exists = reviewService.updateReview(id, updatedReview);
        if (exists) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id ID of the review to be deleted.
     * @return ResponseEntity with HTTP status 200 if deleted, or 404 if the review is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable("id") int id) {
        boolean exists = reviewService.deleteReview(id);
        if (exists) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all reviews or reviews filtered by book ID.
     *
     * @param bookId Optional book ID to filter reviews. If null, all reviews are returned.
     * @return ResponseEntity containing the list of reviews and HTTP status code.
     */
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam(value = "bookId", required = false) Integer bookId) {
        if (bookId == null) {
            bookId = 0;
        }
        List<Review> reviews = reviewService.getAllReviews(bookId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Calculates the average rating for a specific book.
     *
     * @param bookId ID of the book.
     * @return ResponseEntity containing the average rating and HTTP status code.
     */
    @GetMapping("/averageRating")
    public ResponseEntity<Float> calculateAverageRating(@RequestParam int bookId) {
        Float average = reviewService.calculateAverageRating(bookId);
        if (average.isNaN()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(average);
        }
    }

    /**
     * Imports reviews from a JSON string.
     *
     * @param data JSON formatted string containing reviews.
     * @return ResponseEntity with HTTP status 201 if successfully imported, or 400 if an error occurs.
     */
    @PostMapping("/import/json")
    public ResponseEntity<Review> importReviewsFromJSON(@RequestBody String data) {
        try {
            reviewService.importReviewsJSON(data);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Imports reviews from a CSV string.
     *
     * @param data CSV formatted string containing reviews.
     * @return ResponseEntity with HTTP status 201 if successfully imported, or 400 if an error occurs.
     */
    @PostMapping("/import/csv")
    public ResponseEntity<Review> importReviewsFromCSV(@RequestBody String data) {
        try {
            reviewService.importReviewsCSV(data);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Exports reviews to a JSON string for a specific book.
     *
     * @param bookId ID of the book whose reviews are to be exported.
     * @return JSON formatted string containing the reviews.
     */
    @GetMapping("/export/json/{bookId}")
    public String exportReviewsToJSON(@PathVariable int bookId) {
        return reviewService.exportReviewsJSON(bookId);
    }

    /**
     * Exports reviews to a CSV string for a specific book.
     *
     * @param bookId ID of the book whose reviews are to be exported.
     * @return CSV formatted string containing the reviews.
     */
    @GetMapping("/export/csv/{bookId}")
    public String exportReviewsToCSV(@PathVariable int bookId) {
        return reviewService.exportReviewsCSV(bookId);
    }
}
