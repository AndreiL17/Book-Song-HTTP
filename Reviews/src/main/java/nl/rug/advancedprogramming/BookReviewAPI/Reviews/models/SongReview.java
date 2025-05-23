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

@Entity
@Getter
@Setter
public class SongReview implements toJSON, toCSV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;
    private int songId;
    private float rating;
    private String comment;
    private Date date;

    /**
     * Constructor to create a Review with specified details.
     *
     * @param songId  The ID of the song being reviewed.
     * @param rating  The rating given to the book.
     * @param comment The comment provided by the reviewer.
     * @param date    The date the review was made.
     */
    public SongReview(int songId, float rating, String comment, Date date) {
        this.songId = songId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    /**
     * Default constructor for Review. Initializes an empty review object.
     */
    public SongReview() {

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
                getSongId() + "," +
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
                "\t\t\"albumId\":" + getSongId() + ",\n" +
                "\t\t\"rating\":" + getRating() + ",\n" +
                "\t\t\"comment\":\"" + getComment() + "\",\n" +
                "\t\t\"reviewDate\":\"" + getDate() + "\"\n" +
                "\t},\n";
    }
}
