package nl.rug.advancedprogramming.BookReviewAPI.Reviews;

import nl.rug.advancedprogramming.BookReviewAPI.Application.BookReviewApiApplication;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.service.ReviewService;
import nl.rug.advancedprogramming.BookReviewAPI.Reviews.models.Review;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookReviewApiApplication.class)
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // We mock the service throughout the tests
    private ReviewService reviewService;

    @Test
    void testGetReview() throws Exception {
        // Mock the service response
        when(reviewService.getReview(1)).thenReturn(Optional.of(new Review(1, 4, "Great book!", new Date())));

        // Test the GET endpoint
        mockMvc.perform(get("/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.comment").value("Great book!"));
    }

    @Test
    void testGetBookReviews() throws Exception {
        Review review1 = new Review(1, 4, "Great book!", new Date());
        Review review2 = new Review(1, 5, "Amazing!", new Date());

        // Mock the service to return the list of reviews when bookId=1 is passed
        when(reviewService.getAllReviews(1)).thenReturn(Arrays.asList(review1, review2));

        // Perform the request
        mockMvc.perform(get("/reviews").param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expect status 200 OK
                .andExpect(jsonPath("$.length()").value(2))  // Expect 2 reviews in the response
                .andExpect(jsonPath("$[0].bookId").value(1))  // Check first review's bookId
                .andExpect(jsonPath("$[1].bookId").value(1));  // Check second review's

    }

    @Test
    void testAddReview() throws Exception {
        Review review = new Review(1, 5, "Amazing book!", new Date());
        when(reviewService.addReview(any(Review.class))).thenReturn(ResponseEntity.ok(review));

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":1,\"rating\":5,\"comment\":\"Amazing book!\",\"date\":\"2023-10-10\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testDeleteReview() throws Exception {
        when(reviewService.deleteReview(1)).thenReturn(true);

        mockMvc.perform(delete("/reviews/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateReview() throws Exception {
        when(reviewService.updateReview(eq(1), any(Review.class))).thenReturn(true);
        mockMvc.perform(put("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":1,\"rating\":5,\"comment\":\"Updated comment!\",\"date\":\"2023-10-10\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testCalculateAverageRating()  throws Exception {
        when(reviewService.calculateAverageRating(1)).thenReturn(3.5f);

        mockMvc.perform(get("/reviews/averageRating?bookId=1"))
                .andExpect(status().isOk())  // Expect 200 OK
                .andExpect(jsonPath("$").value(3.5));
    }

    @Test
    void testImportReviewsJSON() throws Exception {
        mockMvc.perform(post("/reviews/import/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"bookId\":1,\"rating\":5,\"comment\":\"Excellent!\",\"date\":\"2023-10-10\"}]"))
                .andExpect(status().isCreated());

    }

    @Test
    void testImportReviewsCSV() throws Exception {
        mockMvc.perform(post("/reviews/import/csv")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("bookId,rating,comment,reviewDate\n1,5,\"Excellent!\",\"2023-10-10\"\n"))
                .andExpect(status().isCreated());
    }

    @Test
    void testExportReviewsJSON() throws Exception {
        when(reviewService.exportReviewsJSON(1)).thenReturn("[{\"bookId\":1,\"rating\":5,\"comment\":\"Excellent!\",\"date\":\"2023-10-10\"}]");

        mockMvc.perform(get("/reviews/export/json/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"bookId\":1,\"rating\":5,\"comment\":\"Excellent!\",\"date\":\"2023-10-10\"}]"));

    }

    @Test
    void testExportReviewsCSV() throws Exception {
        when(reviewService.exportReviewsCSV(1)).thenReturn("bookId,rating,comment,reviewDate\n1,5,\"Excellent!\",\"2023-10-10\"\n");

        mockMvc.perform(get("/reviews/export/csv/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("bookId,rating,comment,reviewDate\n1,5,\"Excellent!\",\"2023-10-10\"\n"));
    }
}