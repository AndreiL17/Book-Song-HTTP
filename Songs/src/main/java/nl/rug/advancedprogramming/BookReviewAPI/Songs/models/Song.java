package nl.rug.advancedprogramming.BookReviewAPI.Songs.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a song in the database.
 */
@Setter
@Getter
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String title;
    public String artist;
    public String label;
    public String genre;
    public int length;

    /**
     * Constructor with parameters to initialize a song with specific details.
     *
     * @param id     the ID of the song
     * @param title  the title of the song
     * @param artist the artist of the song
     * @param label  the label of the song
     * @param genre  the genre of the song
     * @param length the length of the song
     */
    public Song(int id, String title, String artist, String label, String genre, int length) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.label = label;
        this.genre = genre;
        this.length = length;
    }

    /**
     * Default constructor required by the framework.
     */
    public Song() {

    }

}
