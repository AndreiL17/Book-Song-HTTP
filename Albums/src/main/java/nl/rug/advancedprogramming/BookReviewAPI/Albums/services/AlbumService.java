package nl.rug.advancedprogramming.BookReviewAPI.Albums.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rug.advancedprogramming.BookReviewAPI.Albums.models.Album;
import nl.rug.advancedprogramming.BookReviewAPI.Albums.repositories.AlbumRepository;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.AlbumReview;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.repository.AlbumReviewRepository;
import nl.rug.advancedprogramming.BookReviewAPI.Songs.models.Song;
import nl.rug.advancedprogramming.BookReviewAPI.Songs.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing albums, providing CRUD operations and data manipulation.
 */
@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumReviewRepository albumReviewRepository;
    private final SongRepository songRepository;

    /**
     * Constructs a new AlbumService.
     *
     * @param albumRepository repository for Album entities.
     * @param albumReviewRepository repository for AlbumReview entities.
     * @param songRepository repository for Song entities.
     */
    public AlbumService(AlbumRepository albumRepository, AlbumReviewRepository albumReviewRepository, SongRepository songRepository) {
        this.albumRepository = albumRepository;
        this.albumReviewRepository = albumReviewRepository;
        this.songRepository = songRepository;
    }

    /**
     * Retrieves all albums.
     *
     * @return an iterable of all albums.
     */
    public Iterable<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    /**
     * Creates a new album.
     *
     * @param album the album to create.
     * @return the saved album.
     */
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    /**
     * Retrieves an album by its ID.
     *
     * @param id the ID of the album.
     * @return the album if found, otherwise null.
     */
    public Album getAlbumById(int id) {
        return albumRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves albums by a specified property and value.
     *
     * @param property the property to filter by (e.g., title, artist, genre).
     * @param value the value of the property.
     * @return an iterable of albums matching the criteria, or null if the property is invalid.
     */
    public Iterable<Album> getAlbumsByProperty(String property, String value) {
        return switch (property) {
            case "title" -> albumRepository.getByTitle(value);
            case "artist" -> albumRepository.getByArtist(value);
            case "genre" -> albumRepository.getByGenre(value);
            default -> null;
        };
    }

    /**
     * Updates an existing album with new details.
     *
     * @param id the ID of the album to update.
     * @param updatedAlbum the updated album details.
     * @return true if the album was updated, false if the album does not exist.
     */
    public boolean updateAlbum(int id, Album updatedAlbum) {
        Optional<Album> toBeUpdated = albumRepository.findById(id);
        if (toBeUpdated.isPresent()) {
            Album existing = toBeUpdated.get();
            existing.setId(updatedAlbum.getId());
            existing.setTitle(updatedAlbum.getTitle());
            existing.setArtist(updatedAlbum.getArtist());
            existing.setGenre(updatedAlbum.getGenre());
            existing.setReleaseDate(updatedAlbum.getReleaseDate());
            existing.setSongIds(updatedAlbum.getSongIds());
            albumRepository.save(existing);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes an album by its ID.
     *
     * @param id the ID of the album to delete.
     * @return true if the album was deleted, false if the album does not exist.
     */
    public boolean deleteAlbum(int id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a song to an album by song ID.
     *
     * @param id the ID of the album.
     * @param songId the ID of the song to add.
     */
    public void addSongToAlbum(int id, int songId) {
        Album album = albumRepository.findById(id).get();
        album.getSongIds().add(songId);
        updateAlbum(id, album);
    }

    /**
     * Removes a song from an album by song ID.
     *
     * @param id the ID of the album.
     * @param songId the ID of the song to remove.
     */
    public void removeSongFromAlbum(int id, int songId) {
        Album album = albumRepository.findById(id).get();
        album.getSongIds().remove((Integer) songId);
        updateAlbum(id, album);
    }

    /**
     * Calculates the average rating for an album based on its reviews.
     *
     * @param id the ID of the album.
     * @return the average rating, or NaN if there are no reviews.
     */
    public double getAlbumRating(int id) {
        List<AlbumReview> reviews = albumReviewRepository.findByAlbumId(id);
        double avgRating = 0;
        int cnt = 0;
        for (AlbumReview review : reviews) {
            avgRating += review.getRating();
            cnt++;
        }
        return avgRating / cnt;
    }

    /**
     * Imports albums from a JSON-formatted string.
     *
     * @param data the JSON data as a string.
     * @return an iterable of imported albums.
     * @throws IOException if an I/O error occurs.
     */
    public Iterable<Album> importAlbumsJSON(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Album> albums;
        albums = objectMapper.readValue(data, new TypeReference<ArrayList<Album>>() {});
        albumRepository.saveAll(albums);
        return albums;
    }

    /**
     * Imports albums from a CSV-formatted string.
     *
     * @param data the CSV data as a string.
     * @return an iterable of imported albums.
     * @throws IOException if an I/O error occurs.
     */
    public Iterable<Album> importAlbumsCSV(String data) throws IOException {
        List<Album> reviews = new ArrayList<>();
        String[] lines = data.split("\\r?\\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String title = fields[0].replace("\"", "").trim();
            String artist = fields[1].replace("\"", "").trim();
            String comment = fields[2].replace("\"", "").trim();
            String reviewDateStr = fields[3].trim();
            java.util.Date reviewDate;
            try {
                reviewDate = dateFormat.parse(reviewDateStr);
                Album review = new Album(title, artist, comment, reviewDate);
                reviews.add(review);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        albumRepository.saveAll(reviews);
        return reviews;
    }

    /**
     * Exports all albums to a JSON-formatted string.
     *
     * @return a JSON string representing all albums.
     */
    public String exportAlbumsJSON() {
        List<Album> toBeExported = (List<Album>) getAllAlbums();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (Album album : toBeExported) {
            sb.append(album.toJSON());
            sb.append("\t\t\"songs\": [\n");
            List<Integer> songIds = album.getSongIds();
            for (Integer songId : songIds) {
                Optional<Song> song = songRepository.findById(songId.toString());
                sb.append("\t\t\t").append(song.get().getTitle()).append(",\n");
            }
            sb.append("\t\t]\n");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Exports all albums to a CSV-formatted string.
     *
     * @return a CSV string representing all albums.
     */
    public String exportAlbumsCSV() {
        List<Album> toBeExported = (List<Album>) getAllAlbums();
        StringBuilder sb = new StringBuilder();
        sb.append("id,title,artist,genre,releaseDate\n");
        for (Album album : toBeExported) {
            sb.append(album.toCSV());
        }
        return sb.toString();
    }
}
