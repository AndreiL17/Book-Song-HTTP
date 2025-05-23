package nl.rug.advancedprogramming.BookReviewAPI.Reviews.controller;

import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.SongReview;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.service.SongReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songReviews")
public class SongReviewController {
    private final SongReviewService songReviewService;

    /**
     * Constructor to initialize the ReviewService.
     *
     * @param reviewService Service layer for review operations.
     */
    public SongReviewController(SongReviewService reviewService) {
        this.songReviewService = reviewService;
    }

    /**
     * Adds a new review.
     *
     * @param review Review to be added.
     * @return ResponseEntity with the created review and HTTP status code.
     */
    @PostMapping
    public ResponseEntity<SongReview> addReview(@RequestBody SongReview review) {
        return songReviewService.addReview(review);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id ID of the review to be retrieved.
     * @return ResponseEntity containing the review if found, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SongReview> getReview(@PathVariable int id) {
        Optional<SongReview> review = songReviewService.getReview(id);
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
    public ResponseEntity<SongReview> updateReview(@PathVariable int id, @RequestBody SongReview updatedReview) {
        boolean exists = songReviewService.updateReview(id, updatedReview);
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
    public ResponseEntity<SongReview> deleteReview(@PathVariable("id") int id) {
        boolean exists = songReviewService.deleteReview(id);
        if (exists) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all reviews or reviews filtered by book ID.
     *
     * @param songId Optional book ID to filter reviews. If null, all reviews are returned.
     * @return ResponseEntity containing the list of reviews and HTTP status code.
     */
    @GetMapping
    public ResponseEntity<List<SongReview>> getAllReviews(@RequestParam(value = "songId", required = false) Integer songId) {
        if (songId == null) {
            songId = 0;
        }
        List<SongReview> reviews = songReviewService.getAllReviews(songId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Calculates the average rating for a specific book.
     *
     * @param songId ID of the book.
     * @return ResponseEntity containing the average rating and HTTP status code.
     */
    @GetMapping("/averageRating")
    public ResponseEntity<Float> calculateAverageRating(@RequestParam int songId) {
        Float average = songReviewService.calculateAverageRating(songId);
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
    public ResponseEntity<SongReview> importReviewsFromJSON(@RequestBody String data) {
        try {
            songReviewService.importReviewsJSON(data);
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
    public ResponseEntity<SongReview> importReviewsFromCSV(@RequestBody String data) {
        try {
            songReviewService.importReviewsCSV(data);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Exports reviews to a JSON string for a specific book.
     *
     * @param songId ID of the book whose reviews are to be exported.
     * @return JSON formatted string containing the reviews.
     */
    @GetMapping("/export/json/{songId}")
    public String exportReviewsToJSON(@PathVariable int songId) {
        return songReviewService.exportReviewsJSON(songId);
    }

    /**
     * Exports reviews to a CSV string for a specific book.
     *
     * @param songId ID of the book whose reviews are to be exported.
     * @return CSV formatted string containing the reviews.
     */
    @GetMapping("/export/csv/{songId}")
    public String exportReviewsToCSV(@PathVariable int songId) {
        return songReviewService.exportReviewsCSV(songId);
    }

}
