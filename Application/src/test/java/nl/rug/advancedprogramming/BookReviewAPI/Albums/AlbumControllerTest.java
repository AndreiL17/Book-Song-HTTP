package nl.rug.advancedprogramming.BookReviewAPI.Albums;

import nl.rug.advancedprogramming.BookReviewAPI.Albums.services.AlbumService;
import nl.rug.advancedprogramming.BookReviewAPI.Application.BookReviewApiApplication;
import nl.rug.advancedprogramming.BookReviewAPI.Albums.models.Album;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookReviewApiApplication.class)
@AutoConfigureMockMvc
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Test
    void testGetAllAlbums_NoParams() throws Exception {
        Date date = new Date();
        Album album = new Album("Whenever You Need Somebody", "Rick Astley", "Pop", date);
        when(albumService.getAllAlbums()).thenReturn(Collections.singletonList(album));

        mockMvc.perform(get("/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Whenever You Need Somebody"))
                .andExpect(jsonPath("$[0].artist").value("Rick Astley"));
    }

    @Test
    void testGetAllAlbums_WithFilter() throws Exception {
        Date date = new Date();
        Album album = new Album("Whenever You Need Somebody", "Rick Astley", "Pop", date);
        when(albumService.getAlbumsByProperty("title", "Whenever You Need Somebody"))
                .thenReturn(Collections.singletonList(album));

        mockMvc.perform(get("/albums")
                        .param("property", "title")
                        .param("value", "Whenever You Need Somebody"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Whenever You Need Somebody"));
    }

    @Test
    void testAddAlbum() throws Exception {
        Date date = new Date();
        Album album = new Album("Whenever You Need Somebody", "Rick Astley", "Pop", date);
        when(albumService.createAlbum(any(Album.class))).thenReturn(album);

        mockMvc.perform(post("/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Whenever You Need Somebody\",\"artist\":\"Rick Astley\",\"releaseDate\":\"1987-07-27\",\"genre\":\"Pop\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Whenever You Need Somebody"));
    }

    @Test
    void testGetAlbumById_Found() throws Exception {
        Date date = new Date();
        Album album = new Album("Whenever You Need Somebody", "Rick Astley", "Pop", date);
        when(albumService.getAlbumById(1)).thenReturn(album);

        mockMvc.perform(get("/albums/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Whenever You Need Somebody"));
    }

    @Test
    void testGetAlbumById_NotFound() throws Exception {
        when(albumService.getAlbumById(1)).thenReturn(null);

        mockMvc.perform(get("/albums/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateAlbum() throws Exception {
        when(albumService.updateAlbum(eq(1), any(Album.class))).thenReturn(true);

        mockMvc.perform(put("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Album\",\"artist\":\"Updated Artist\",\"releaseDate\":\"1987-07-27\",\"genre\":\"Updated Genre\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Album"));
    }

    @Test
    void testDeleteAlbum() throws Exception {
        when(albumService.deleteAlbum(1)).thenReturn(true);

        mockMvc.perform(delete("/albums/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddSongToAlbum() throws Exception {
        doNothing().when(albumService).addSongToAlbum(1, 100);

        mockMvc.perform(post("/albums/1/songs/100"))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveSongFromAlbum() throws Exception {
        doNothing().when(albumService).removeSongFromAlbum(1, 100);

        mockMvc.perform(delete("/albums/1/songs/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAlbumRating() throws Exception {
        when(albumService.getAlbumRating(1)).thenReturn(4.5);

        mockMvc.perform(get("/albums/1/rating"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }

    @Test
    void testGetAlbumRating_NoRating() throws Exception {
        when(albumService.getAlbumRating(1)).thenReturn(Double.NaN);

        mockMvc.perform(get("/albums/1/rating"))
                .andExpect(status().isNoContent());
    }
}