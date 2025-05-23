package nl.rug.advancedprogramming.BookReviewAPI.Reviews.controller;

import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.AlbumReview;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.service.AlbumReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albumReviews")
public class AlbumReviewController {
    private final AlbumReviewService albumReviewService;

    /**
     * Constructor to initialize the ReviewService.
     *
     * @param reviewService Service layer for review operations.
     */
    public AlbumReviewController(AlbumReviewService reviewService) {
        this.albumReviewService = reviewService;
    }

    /**
     * Adds a new review.
     *
     * @param review Review to be added.
     * @return ResponseEntity with the created review and HTTP status code.
     */
    @PostMapping
    public ResponseEntity<AlbumReview> addReview(@RequestBody AlbumReview review) {
        return albumReviewService.addReview(review);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id ID of the review to be retrieved.
     * @return ResponseEntity containing the review if found, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlbumReview> getReview(@PathVariable int id) {
        Optional<AlbumReview> review = albumReviewService.getReview(id);
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
    public ResponseEntity<AlbumReview> updateReview(@PathVariable int id, @RequestBody AlbumReview updatedReview) {
        boolean exists = albumReviewService.updateReview(id, updatedReview);
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
    public ResponseEntity<AlbumReview> deleteReview(@PathVariable("id") int id) {
        boolean exists = albumReviewService.deleteReview(id);
        if (exists) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all reviews or reviews filtered by book ID.
     *
     * @param albumId Optional book ID to filter reviews. If null, all reviews are returned.
     * @return ResponseEntity containing the list of reviews and HTTP status code.
     */
    @GetMapping
    public ResponseEntity<List<AlbumReview>> getAllReviews(@RequestParam(value = "albumId", required = false) Integer albumId) {
        if (albumId == null) {
            albumId = 0;
        }
        List<AlbumReview> reviews = albumReviewService.getAllReviews(albumId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Calculates the average rating for a specific book.
     *
     * @param albumId ID of the book.
     * @return ResponseEntity containing the average rating and HTTP status code.
     */
    @GetMapping("/averageRating")
    public ResponseEntity<Float> calculateAverageRating(@RequestParam int albumId) {
        Float average = albumReviewService.calculateAverageRating(albumId);
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
    public ResponseEntity<AlbumReview> importReviewsFromJSON(@RequestBody String data) {
        try {
            albumReviewService.importReviewsJSON(data);
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
    public ResponseEntity<AlbumReview> importReviewsFromCSV(@RequestBody String data) {
        try {
            albumReviewService.importReviewsCSV(data);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Exports reviews to a JSON string for a specific book.
     *
     * @param albumId ID of the book whose reviews are to be exported.
     * @return JSON formatted string containing the reviews.
     */
    @GetMapping("/export/json/{albumId}")
    public String exportReviewsToJSON(@PathVariable int albumId) {
        return albumReviewService.exportReviewsJSON(albumId);
    }

    /**
     * Exports reviews to a CSV string for a specific book.
     *
     * @param albumId ID of the book whose reviews are to be exported.
     * @return CSV formatted string containing the reviews.
     */
    @GetMapping("/export/csv/{albumId}")
    public String exportReviewsToCSV(@PathVariable int albumId) {
        return albumReviewService.exportReviewsCSV(albumId);
    }

}
