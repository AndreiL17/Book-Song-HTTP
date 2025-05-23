package nl.rug.advancedprogramming.BookReviewAPI.Albums.controllers;

import nl.rug.advancedprogramming.BookReviewAPI.Albums.models.Album;
import nl.rug.advancedprogramming.BookReviewAPI.Albums.services.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for handling requests related to albums, including creating, retrieving, updating,
 * and deleting albums, as well as importing/exporting album data and managing album songs.
 */
@RestController
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumService albumService;

    /**
     * Constructs a new AlbumController with the specified AlbumService.
     *
     * @param albumService the service that provides album-related operations.
     */
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    /**
     * Creates a new album.
     *
     * @param album the album to create.
     * @return a ResponseEntity containing the created album and HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        Album created = albumService.createAlbum(album);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves an album by its ID.
     *
     * @param id the ID of the album to retrieve.
     * @return a ResponseEntity containing the album and HTTP status OK if found, or NOT_FOUND if not.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable int id) {
        Album album = albumService.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(album);
    }

    /**
     * Retrieves a list of albums, either all albums or filtered by a specific property.
     *
     * @param property the property to filter by (e.g., "title", "artist").
     * @param value    the value of the property to match.
     * @return a ResponseEntity containing the list of matching albums and HTTP status OK, or BAD_REQUEST if missing value.
     */
    @GetMapping
    public ResponseEntity<List<Album>> getAlbums(@RequestParam Optional<String> property, @RequestParam Optional<String> value) {
        if (property.isEmpty()) {
            List<Album> list = (List<Album>) albumService.getAllAlbums();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        if (value.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Album> albums = (List<Album>) albumService.getAlbumsByProperty(property.get(), value.get());
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    /**
     * Updates an existing album by its ID.
     *
     * @param id    the ID of the album to update.
     * @param album the updated album data.
     * @return a ResponseEntity containing the updated album and HTTP status OK if successful, or NOT_FOUND if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable int id, @RequestBody Album album) {
        if (albumService.updateAlbum(id, album)) {
            return ResponseEntity.status(HttpStatus.OK).body(album);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Deletes an album by its ID.
     *
     * @param id the ID of the album to delete.
     * @return a ResponseEntity with HTTP status NO_CONTENT if successful, or NOT_FOUND if the album does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable int id) {
        boolean exists = albumService.deleteAlbum(id);
        if (exists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a song to an album.
     *
     * @param id     the ID of the album.
     * @param songId the ID of the song to add.
     * @return a ResponseEntity with HTTP status OK after adding the song.
     */
    @PostMapping("/{id}/songs/{songId}")
    public ResponseEntity<Album> addSongToAlbum(@PathVariable int id, @PathVariable int songId) {
        albumService.addSongToAlbum(id, songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Removes a song from an album.
     *
     * @param id     the ID of the album.
     * @param songId the ID of the song to remove.
     * @return a ResponseEntity with HTTP status NO_CONTENT after removing the song.
     */
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<Album> removeSongFromAlbum(@PathVariable int id, @PathVariable int songId) {
        albumService.removeSongFromAlbum(id, songId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves the average rating of an album.
     *
     * @param id the ID of the album.
     * @return a ResponseEntity containing the average rating and HTTP status OK, or NO_CONTENT if no ratings are found.
     */
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getAlbumRating(@PathVariable int id) {
        Double average = albumService.getAlbumRating(id);
        if (average.isNaN()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(average);
        }
    }

    /**
     * Imports album data from a JSON string.
     *
     * @param data the JSON-formatted string containing album data.
     * @return a ResponseEntity containing the list of imported albums and HTTP status CREATED, or BAD_REQUEST if invalid data.
     */
    @PostMapping("/import/json")
    public ResponseEntity<List<Album>> importAlbumsJSON(@RequestBody String data) {
        try {
            List<Album> albums = (List<Album>) albumService.importAlbumsJSON(data);
            return new ResponseEntity<>(albums, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Imports album data from a CSV string.
     *
     * @param data the CSV-formatted string containing album data.
     * @return a ResponseEntity containing the list of imported albums and HTTP status CREATED, or BAD_REQUEST if invalid data.
     */
    @PostMapping("/import/csv")
    public ResponseEntity<List<Album>> importAlbumsCSV(@RequestBody String data) {
        try {
            List<Album> albums = (List<Album>) albumService.importAlbumsCSV(data);
            return new ResponseEntity<>(albums, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Exports all albums to a JSON string.
     *
     * @return a JSON-formatted string of all albums.
     */
    @GetMapping("/export/json")
    public String exportAlbumsJSON() {
        return albumService.exportAlbumsJSON();
    }

    /**
     * Exports all albums to a CSV string.
     *
     * @return a CSV-formatted string of all albums.
     */
    @GetMapping("/export/csv")
    public String exportAlbumsCSV() {
        return albumService.exportAlbumsCSV();
    }
}
