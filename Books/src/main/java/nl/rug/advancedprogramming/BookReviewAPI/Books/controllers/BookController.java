package nl.rug.advancedprogramming.BookReviewAPI.Books.controllers;

import nl.rug.advancedprogramming.BookReviewAPI.Books.models.Book;
import nl.rug.advancedprogramming.BookReviewAPI.Books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService _bookService;

    @Autowired
    public BookController(BookService bookService) {
        _bookService = bookService;
    }

    /**
     * Retrieves books from database based on specified property/key and its value.
     * If the function parameters are not filled in, retrieves all books from database.
     * This deviates from the specification because getAllBooks() and getByProperty() use the same endpoint.
     * So the framework NEEDS it to be in 1 function. Otherwise, it clashes with each other.
     *
     * @param property The property to search by.
     * @param value The value of the property.
     * @return A {@link ResponseEntity} containing books that match given key/value with 200 OK status code. 400 BADREQUEST if malformed request.
     */
    @GetMapping("")
    public ResponseEntity<Iterable<Book>> getBooks(@RequestParam Optional<String> property, @RequestParam Optional<String> value) {

        // We don't want to query by property. So get all books
        if (property.isEmpty()) {
            return ResponseEntity.ok(_bookService.getAllBooks());
        }

        // A property is defined but value is not?
        if (value.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Iterable<Book> result = _bookService.getByProperty(property.get(), value.get());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * Adds a book to database.
     *
     * @param book Book to add.
     * @return A {@link ResponseEntity} containing the book that was added with 201 CREATED HTTP status code.
     */
    @PostMapping("")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        _bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /**
     * Removes a book from database.
     *
     * @param isbn ISBN of the Book to remove.
     * @return A {@link ResponseEntity} with 204 NO CONTENT HTTP status code.
     */
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        _bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates a book in database.
     *
     * @param isbn ISBN of the Book to update.
     * @param book Updated book.
     * @return A {@link ResponseEntity} with 200 OK HTTP status code.
     */
    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody Book book) {
        _bookService.updateBook(isbn, book);
        return ResponseEntity.ok(book);
    }

    /**
     * Imports books from a JSON or CSV file to add to database. It's recommended to put an absolute path, like
     * C:\Users\Julian\Desktop\testing.json etc.
     *
     * @param filePath Name of the filePath to import the books from.
     * @return A {@link ResponseEntity} with 200 OK HTTP status code. 400 BADREQUEST if filePath is not found.
     */
    @PostMapping("/import")
    public ResponseEntity<Iterable<Book>> importBooks(@RequestParam String filePath) {
        try {
            _bookService.importBooks(filePath);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(null);
    }

    /**
     * Exports given books in JSON or CSV format.
     *
     * @param format JSON or CSV. The format to export in. Case insensitive.
     * @param books Books to export.
     * @return A {@link ResponseEntity} containg the books in specified format 200 OK HTTP status code.
     * 400 BADREQUEST if unsupported format.
     */
    @GetMapping("/export")
    public ResponseEntity<String> exportBooks(@RequestParam String format, @RequestBody Iterable<Book> books) {
        return switch (format.toLowerCase()) {
            case "csv" -> ResponseEntity.ok(_bookService.exportCSV(books));
            case "json" -> ResponseEntity.ok(_bookService.exportJSON(books));
            default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Format not supported: " + format);
        };
    }
}
