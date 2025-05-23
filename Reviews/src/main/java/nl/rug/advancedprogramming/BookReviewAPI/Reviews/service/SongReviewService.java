package nl.rug.advancedprogramming.BookReviewAPI.Reviews.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.SongReview;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.repository.SongReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongReviewService {
    private final SongReviewRepository songReviewRepository;

    /**
     * Constructor for the ReviewService.
     *
     * @param songReviewRepository the repository for accessing review data
     */
    public SongReviewService(SongReviewRepository songReviewRepository) {
        this.songReviewRepository = songReviewRepository;
    }

    /**
     * Adds a new review to the repository.
     *
     * @param review the review to be added
     * @return a ResponseEntity with the appropriate HTTP status
     */
    public ResponseEntity<SongReview> addReview(SongReview review) {
        songReviewRepository.save(review);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Fetches a review by its ID.
     *
     * @param id the ID of the review
     * @return an Optional containing the review if found, or empty if not
     */
    public Optional<SongReview> getReview(int id) {
        return songReviewRepository.findById(id);
    }

    /**
     * Updates an existing review by its ID with new data.
     *
     * @param reviewId      the ID of the review to update
     * @param updatedReview the new review data
     * @return true if the review was successfully updated, false otherwise
     */
    public boolean updateReview(int reviewId, SongReview updatedReview) {
        Optional<SongReview> toBeUpdated = songReviewRepository.findById(reviewId);
        if (toBeUpdated.isPresent()) {
            SongReview existingReview = toBeUpdated.get();
            existingReview.setReviewId(updatedReview.getReviewId());
            existingReview.setSongId(updatedReview.getSongId());
            existingReview.setRating(updatedReview.getRating());
            existingReview.setComment(updatedReview.getComment());
            songReviewRepository.save(existingReview);
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
        Optional<SongReview> toBeDeleted = songReviewRepository.findById(reviewId);
        if (toBeDeleted.isPresent()) {
            songReviewRepository.deleteById(reviewId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves all reviews, optionally filtered by song ID.
     *
     * @param songId the ID of the song to filter reviews by, or 0 for all reviews
     * @return a list of reviews
     */
    public List<SongReview> getAllReviews(int songId) {
        if (songId < 1) {
            return (List<SongReview>) songReviewRepository.findAll();
        }
        return songReviewRepository.findBySongId(songId);
    }

    /**
     * Calculates the average rating for a songId by its ID.
     *
     * @param songId the ID of the song to calculate the average rating for
     * @return the average rating of the song
     */
    public float calculateAverageRating(int songId) {
        List<SongReview> reviews = getAllReviews(songId);
        float sum = 0;
        for (SongReview review : reviews) {
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
        List<SongReview> reviews;
        reviews = objectMapper.readValue(data, new TypeReference<ArrayList<SongReview>>() {
        });
        songReviewRepository.saveAll(reviews);
    }

    /**
     * Imports reviews from a CSV string and saves them in the repository.
     *
     * @param data the CSV string containing the review data
     * @throws IOException if an error occurs during parsing
     */
    public void importReviewsCSV(String data) throws IOException {
        List<SongReview> reviews = new ArrayList<>();
        String[] lines = data.split("\\r?\\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            int songId = Integer.parseInt(fields[0].trim());
            float rating = Float.parseFloat(fields[1].trim());
            String comment = fields[2].replace("\"", "").trim();
            String reviewDateStr = fields[3].trim();
            java.util.Date reviewDate;
            try {
                reviewDate = dateFormat.parse(reviewDateStr);
                SongReview review = new SongReview(songId, rating, comment, reviewDate);
                reviews.add(review);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        songReviewRepository.saveAll(reviews);
    }

    /**
     * Exports reviews in JSON format for a specific song.
     *
     * @param songId the ID of the song whose reviews are to be exported
     * @return a JSON string representing the reviews
     */
    public String exportReviewsJSON(int songId) {
        List<SongReview> toBeExported = getAllReviews(songId);
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (SongReview review : toBeExported) {
            sb.append(review.toJSON());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Exports reviews in CSV format for a specific song.
     *
     * @param songId the ID of the song whose reviews are to be exported
     * @return a CSV string representing the reviews
     */
    public String exportReviewsCSV(int songId) {
        List<SongReview> toBeExported = getAllReviews(songId);
        StringBuilder sb = new StringBuilder();
        sb.append("reviewId,songId,rating,comment,reviewDate\n");
        for (SongReview review : toBeExported) {
            sb.append(review.toCSV());
        }
        return sb.toString();
    }
}
