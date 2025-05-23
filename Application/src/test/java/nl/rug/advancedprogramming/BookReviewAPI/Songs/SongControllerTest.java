// SongControllerTest.java

package nl.rug.advancedprogramming.BookReviewAPI.Songs;

import nl.rug.advancedprogramming.BookReviewAPI.Songs.controller.SongController;
import nl.rug.advancedprogramming.BookReviewAPI.Songs.models.Song;
import nl.rug.advancedprogramming.BookReviewAPI.Songs.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SongControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SongService songService;

    @InjectMocks
    private SongController songController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
    }

    @Test
    void testAddSong() throws Exception {
        // Arrange
        Song song = new Song();
        song.id = 1;
        song.title = "Test Song";
        song.artist = "Test Artist";
        song.label = "Test Label";
        song.genre = "Test Genre";
        song.length = 300;

        // Act & Assert
        mockMvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songToJson(song)))
                .andExpect(status().isCreated());

        verify(songService, times(1)).addSong(any(Song.class));
    }

    @Test
    void testGetAllSongs() throws Exception {
        // Arrange
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song();
        song1.id = 1;
        song1.title = "Song 1";
        song1.artist = "Artist 1";
        song1.label = "Label 1";
        song1.genre = "Genre 1";
        song1.length = 200;
        songs.add(song1);

        Song song2 = new Song();
        song2.id = 2;
        song2.title = "Song 2";
        song2.artist = "Artist 2";
        song2.label = "Label 2";
        song2.genre = "Genre 2";
        song2.length = 250;
        songs.add(song2);

        when(songService.getAllSongs()).thenReturn(songs);

        // Act & Assert
        mockMvc.perform(get("/songs"))
                .andExpect(status().isOk())
                .andExpect(content().json(listToJson(songs)));

        verify(songService, times(1)).getAllSongs();
    }

    @Test
    void testUpdateSong() throws Exception {
        // Arrange
        int songId = 1;
        Song song = new Song();
        song.title = "Updated Title";
        song.artist = "Updated Artist";
        song.label = "Updated Label";
        song.genre = "Updated Genre";
        song.length = 320;

        // Act & Assert
        mockMvc.perform(put("/songs/" + songId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songToJson(song)))
                .andExpect(status().isOk());

        verify(songService, times(1)).updateSong(eq(songId), any(Song.class));
    }

    @Test
    void testDeleteSong() throws Exception {
        // Arrange
        int songId = 1;

        // Act & Assert
        mockMvc.perform(delete("/songs/" + songId))
                .andExpect(status().isNoContent());

        verify(songService, times(1)).deleteSong(songId);
    }

    @Test
    void testGetByProperty_NoProperty() throws Exception {
        // Arrange
        List<Song> songs = new ArrayList<>();
        when(songService.getAllSongs()).thenReturn(songs);

        // Act & Assert
        mockMvc.perform(get("/songs/search"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(songService, times(1)).getAllSongs();
    }

    @Test
    void testGetByProperty_NoValue() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/songs/search")
                        .param("property", "title"))
                .andExpect(status().isBadRequest());

        verify(songService, never()).getByProperty(anyString(), anyString());
    }

    @Test
    void testGetByProperty_Success() throws Exception {
        // Arrange
        List<Song> songs = new ArrayList<>();
        Song song = new Song();
        song.id = 1;
        song.title = "Test Song";
        song.artist = "Test Artist";
        song.label = "Test Label";
        song.genre = "Test Genre";
        song.length = 300;
        songs.add(song);

        when(songService.getByProperty("title", "Test Song")).thenReturn(songs);

        // Act & Assert
        mockMvc.perform(get("/songs/search")
                        .param("property", "title")
                        .param("value", "Test Song"))
                .andExpect(status().isOk())
                .andExpect(content().json(listToJson(songs)));

        verify(songService, times(1)).getByProperty("title", "Test Song");
    }

    @Test
    void testGetByProperty_BadRequest() throws Exception {
        // Arrange
        when(songService.getByProperty("invalidProperty", "value")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/songs/search")
                        .param("property", "invalidProperty")
                        .param("value", "value"))
                .andExpect(status().isBadRequest());

        verify(songService, times(1)).getByProperty("invalidProperty", "value");
    }

    // Helper methods to convert Song objects to JSON
    private String songToJson(Song song) {
        return "{" +
                "\"id\":" + song.id + "," +
                "\"title\":\"" + escapeJson(song.title) + "\"," +
                "\"artist\":\"" + escapeJson(song.artist) + "\"," +
                "\"label\":\"" + escapeJson(song.label) + "\"," +
                "\"genre\":\"" + escapeJson(song.genre) + "\"," +
                "\"length\":" + song.length +
                "}";
    }

    private String listToJson(List<Song> songs) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < songs.size(); i++) {
            json.append(songToJson(songs.get(i)));
            if (i < songs.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\"");
    }
}
