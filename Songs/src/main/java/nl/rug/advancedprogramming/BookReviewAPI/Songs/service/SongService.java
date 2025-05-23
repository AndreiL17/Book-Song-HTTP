package nl.rug.advancedprogramming.BookReviewAPI.Songs.service;

import nl.rug.advancedprogramming.BookReviewAPI.Songs.models.Song;
import nl.rug.advancedprogramming.BookReviewAPI.Songs.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class that provides methods for managing songs.
 */
@Service
public class SongService {
    private final SongRepository songs;

    /**
     * Constructor for SongService.
     *
     * @param songs the SongRepository used to interact with the song database
     */
    @Autowired
    public SongService(SongRepository songs) {
        this.songs = songs;
    }

    /**
     * Adds a new song to the repository.
     *
     * @param song the Song object to be added
     */
    public void addSong(Song song) {
        songs.save(song);
    }

    /**
     * Retrieves all songs from the repository.
     *
     * @return an {@link Iterable} of all Song objects
     */
    public Iterable<Song> getAllSongs() {
        return songs.findAll();
    }

    /**
     * Updates an existing song with new information.
     *
     * @param id   the ID of the song to update
     * @param song the Song object containing updated details
     */
    public void updateSong(int id, Song song) {
        song.id = id;
        songs.save(song);
    }

    /**
     * Deletes a song from the repository by ID.
     *
     * @param id the ID of the song to delete
     */
    public void deleteSong(int id) {
        // id has to be a string for the method to work
        songs.deleteById(String.valueOf(id));
    }

    /**
     * Retrieves songs based on a specified property and value.
     *
     * @param key   the property name to filter by (e.g. "title", "artist")
     * @param value the value of the property to search for
     * @return an {@link Iterable} of Song objects that match the property and value
     */
    public Iterable<Song> getByProperty(String key, String value) {
        // Parse property names into the right repository function
        return switch (key) {
            case "id" -> songs.getById(Integer.parseInt(value));
            case "title" -> songs.getByTitle(value);
            case "artist" -> songs.getByArtist(value);
            case "label" -> songs.getByLabel(value);
            case "genre" -> songs.getByGenre(value);
            case "length" -> songs.getByLength(Integer.parseInt(value));
            default -> null;
        };
    }

}
