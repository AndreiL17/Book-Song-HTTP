package nl.rug.advancedprogramming.BookReviewAPI.Reviews.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.interfaces.toCSV;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.interfaces.toJSON;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Review class represents a review entity for a book. It contains information such as
 * the review ID, book ID, rating, comment, and date of the review. The class also implements
 * the toCSV and toJSON interfaces, allowing reviews to be represented in CSV and JSON formats.
 */
@Entity
@Getter
@Setter
public class Review implements toJSON, toCSV {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;
    private int bookId;
    private float rating;
    private String comment;
    private Date date;

    /**
     * Constructor to create a Review with specified details.
     *
     * @param bookId  The ID of the book being reviewed.
     * @param rating  The rating given to the book.
     * @param comment The comment provided by the reviewer.
     * @param date    The date the review was made.
     */
    public Review(int bookId, float rating, String comment, Date date) {
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    /**
     * Default constructor for Review. Initializes an empty review object.
     */
    public Review() {

    }

    /**
     * Converts the Review object into a CSV (Comma-Separated Values) format string.
     *
     * @return A CSV representation of the review.
     */
    @Override
    public String toCSV() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getReviewId() + "," +
                getBookId() + "," +
                getRating() + "," +
                "\"" + getComment().replace("\"", "\"\"") + "\"," +
                dateFormat.format(getDate()) + "\n";
    }

    /**
     * Converts the Review object into a JSON (JavaScript Object Notation) format string.
     *
     * @return A JSON representation of the review.
     */
    @Override
    public String toJSON() {
        return "\t{\n" +
                "\t\t\"reviewId\":" + getReviewId() + ",\n" +
                "\t\t\"bookId\":" + getBookId() + ",\n" +
                "\t\t\"rating\":" + getRating() + ",\n" +
                "\t\t\"comment\":\"" + getComment() + "\",\n" +
                "\t\t\"reviewDate\":\"" + getDate() + "\"\n" +
                "\t},\n";
    }
}
