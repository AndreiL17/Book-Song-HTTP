package nl.rug.advancedprogramming.BookReviewAPI.Songs.controller;

import nl.rug.advancedprogramming.BookReviewAPI.Songs.models.Song;
import nl.rug.advancedprogramming.BookReviewAPI.Songs.service.SongService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller class for handling CRUD operations on songs.
 */
@RestController
@RequestMapping("")
public class SongController {
    private final SongService songService;

    /**
     * Constructor for SongController.
     *
     * @param songService the SongService used to manage song data
     */
    public SongController(SongService songService) {
        this.songService = songService;
    }

    /**
     * Adds a new song to the database.
     *
     * @param song The song to add.
     * @return A {@link ResponseEntity} with a 201 CREATED HTTP status.
     */
    @PostMapping("/songs")
    public ResponseEntity<Void> addSong(@RequestBody Song song) {
        songService.addSong(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * Retrieves all songs from the database.
     *
     * @return A {@link ResponseEntity} containing an Iterable of all songs with a 200 OK status.
     */
    @GetMapping("/songs")
    public ResponseEntity<Iterable<Song>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    /**
     * Updates an existing song by ID.
     *
     * @param id   the ID of the song to update
     * @param song the Song object with updated information
     * @return {@link ResponseEntity} with a 200 OK HTTP status.
     */
    @PutMapping("/songs/{id}")
    public ResponseEntity<Void> updateSong(@PathVariable int id, @RequestBody Song song) {
        songService.updateSong(id, song);
        return ResponseEntity.ok(null);
    }

    /**
     * Deletes a song by ID.
     *
     * @param id the ID of the song to delete
     * @return {@link ResponseEntity} with a 204 NO CONTENT HTTP status.
     */
    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable int id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves songs based on a specified property and value.
     *
     * @param property the property to search by (e.g. "title", "artist")
     * @param value    the value to search for
     * @return {@link ResponseEntity} containing songs matching the property and value with a 200 OK status,
     * or a 400 BAD REQUEST if the request is malformed.
     */
    @GetMapping("/songs/search")
    public ResponseEntity<Iterable<Song>> getByProperty(@RequestParam Optional<String> property, @RequestParam Optional<String> value) {
        // We don't want to query by property. So get all songs
        if (property.isEmpty()) {
            return ResponseEntity.ok(songService.getAllSongs());
        }

        // A property is defined but value is not?
        if (value.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Iterable<Song> result = songService.getByProperty(property.get(), value.get());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(result);
    }

}
