package nl.rug.advancedprogramming.BookReviewAPI.Albums.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nl.rug.advancedprogramming.BookReviewAPI.Albums.interfaces.toCSV;
import nl.rug.advancedprogramming.BookReviewAPI.Albums.interfaces.toJSON;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a music album with attributes such as title, artist, genre, release date,
 * and a list of associated song IDs. Provides functionality to serialize the album to
 * JSON and CSV formats.
 */
@Setter
@Getter
@Entity
public class Album implements toJSON, toCSV {

    /** The unique identifier for the album, automatically generated. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String artist;
    private String genre;
    private Date releaseDate;

    @ElementCollection
    private List<Integer> songIds;

    /**
     * Constructs a new Album with the specified title, artist, genre, and release date.
     * Initializes an empty list of song IDs.
     *
     * @param title the title of the album.
     * @param artist the artist of the album.
     * @param genre the genre of the album.
     * @param releaseDate the release date of the album.
     */
    public Album(String title, String artist, String genre, Date releaseDate) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.songIds = new ArrayList<>();
    }

    /**
     * Default constructor for JPA.
     */
    public Album() {
    }

    /**
     * Converts the album data to a CSV-formatted string.
     *
     * @return a CSV representation of the album, including id, title, artist, genre, and release date.
     */
    @Override
    public String toCSV() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getId() + "," +
                getTitle() + "," +
                getArtist() + "," +
                getGenre() + ", " +
                dateFormat.format(getReleaseDate()) + "\n";
    }

    /**
     * Converts the album data to a JSON-formatted string.
     *
     * @return a JSON representation of the album, including id, title, artist, genre, and release date.
     */
    @Override
    public String toJSON() {
        return "\t{\n" +
                "\t\t\"id\":" + getId() + ",\n" +
                "\t\t\"title\":\"" + getTitle() + "\",\n" +
                "\t\t\"artist\":\"" + getArtist() + "\",\n" +
                "\t\t\"genre\":\"" + getGenre() + "\",\n" +
                "\t\t\"releaseDate\":\"" + getReleaseDate() + "\"\n";
    }
}
