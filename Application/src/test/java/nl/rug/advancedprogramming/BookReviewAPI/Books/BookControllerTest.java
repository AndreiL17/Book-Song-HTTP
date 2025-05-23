package nl.rug.advancedprogramming.BookReviewAPI.Books;

import nl.rug.advancedprogramming.BookReviewAPI.Application.BookReviewApiApplication;
import nl.rug.advancedprogramming.BookReviewAPI.Books.models.Book;
import nl.rug.advancedprogramming.BookReviewAPI.Books.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookReviewApiApplication.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testGetBooks_NoParams() throws Exception {
        Book book = new Book("Sample Book", "Author Name", "Publisher", "1234567890", "Genre", 29.99);
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Book"))  // Check the title of the returned book
                .andExpect(jsonPath("$[0].author").value("Author Name")); // Check the author of the returned book
    }

    @Test
    void testGetBooks_WithValidProperty() throws Exception {
        Book book = new Book("Sample Book", "Author Name", "Publisher", "1234567890", "Genre", 29.99);
        when(bookService.getByProperty("author", "Author Name")).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books")
                        .param("property", "author")
                        .param("value", "Author Name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Book")); // Check the title of the returned book
    }

    @Test
    void testGetBooks_WithInvalidProperty() throws Exception {
        mockMvc.perform(get("/api/books")
                        .param("property", "author")
                        .param("nan", "ha"))
                .andExpect(status().isBadRequest());  // Expect 400 BAD REQUEST
    }

    @Test
    void testAddBook() throws Exception {
        // Mock the service method to do nothing (void return type)
        doNothing().when(bookService).addBook(any(Book.class));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sample Book\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"isbn\":\"1234567890\",\"genre\":\"Genre\",\"price\":29.99}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Book"));  // Check added book title
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook("1234567890");

        mockMvc.perform(delete("/api/books/{isbn}", "1234567890"))
                .andExpect(status().isNoContent());  // Expect 204 NO CONTENT
    }

    @Test
    void testUpdateBook() throws Exception {
        mockMvc.perform(put("/api/books/{isbn}", "1234567890")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\",\"author\":\"Updated Author\",\"publisher\":\"Updated Publisher\",\"isbn\":\"1234567890\",\"genre\":\"Updated Genre\",\"price\":19.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));  // Check updated book title
    }

    @Test
    void testImportBooks_Success() throws Exception {
        mockMvc.perform(post("/api/books/import")
                        .param("filePath", "test/path/to/file.json"))
                .andExpect(status().isOk());  // Expect 200 OK
    }

    @Test
    void testImportBooks_FileNotFound() throws Exception {
        // Mock the service to throw FileNotFoundException when importBooks is called
        doThrow(new FileNotFoundException()).when(bookService).importBooks("h");
        // Perform the request
        mockMvc.perform(post("/api/books/import")
                        .param("filePath", "h"))
                .andExpect(status().isBadRequest());  // Expect 400 BAD REQUEST
    }

    @Test
    void testExportBooks_CSVFormat() throws Exception {
        // Mock the service response for CSV export
        when(bookService.exportCSV(any())).thenReturn("title,author,publisher,isbn,genre,price\n\"Sample Book\",\"Author Name\",\"Publisher\",\"1234567890\",\"Genre\",29.99");

        // Perform the request with a body
        mockMvc.perform(get("/api/books/export")
                        .param("format", "csv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"title\":\"Sample Book\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"isbn\":\"1234567890\",\"genre\":\"Genre\",\"price\":29.99}]"))
                .andExpect(status().isOk())
                .andExpect(content().string("title,author,publisher,isbn,genre,price\n\"Sample Book\",\"Author Name\",\"Publisher\",\"1234567890\",\"Genre\",29.99"));
    }

    @Test
    void testExportBooks_JSONFormat() throws Exception {
        // Mock the service response for JSON export
        when(bookService.exportJSON(any())).thenReturn("[{\"title\":\"Sample Book\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"isbn\":\"1234567890\",\"genre\":\"Genre\",\"price\":29.99}]");

        // Perform the request with a body
        mockMvc.perform(get("/api/books/export")
                        .param("format", "json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"title\":\"Sample Book\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"isbn\":\"1234567890\",\"genre\":\"Genre\",\"price\":29.99}]"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"title\":\"Sample Book\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"isbn\":\"1234567890\",\"genre\":\"Genre\",\"price\":29.99}]"));
    }


    @Test
    void testExportBooks_InvalidFormat() throws Exception {
        mockMvc.perform(get("/api/books/export")
                        .param("format", "txt"))
                .andExpect(status().isBadRequest());
    }
}
