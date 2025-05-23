package nl.rug.advancedprogramming.BookReviewAPI.Books.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import nl.rug.advancedprogramming.BookReviewAPI.Books.models.Book;
import nl.rug.advancedprogramming.BookReviewAPI.Books.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Service
public class BookService {
    private final BookRepository _books;

    @Autowired
    public BookService(BookRepository _books) {
        this._books = _books;
    }

    /**
     * Retrieves all books from database.
     *
     * @return an {@link Iterable} containing all books in the database.
     */
    public Iterable<Book> getAllBooks() {
        return _books.findAll();
    }

    /**
     * Retrieves books from database based on specified property/key and its value.
     *
     * @param key The property to search by
     * @param value The value of the property
     * @return an {@link Iterable} containing books that match given key/value
     */
    public Iterable<Book> getByProperty(String key, String value) {
        // Parse property names into the right repository function
        return switch (key) {
            case "title" -> _books.getByTitle(value);
            case "author" -> _books.getByAuthor(value);
            case "publisher" -> _books.getByPublisher(value);
            case "isbn" -> _books.getByIsbn(value);
            case "genre" -> _books.getByGenre(value);
            case "price" -> _books.getByPrice(Double.parseDouble(value));
            default -> null;
        };
    }

    /**
     * Adds a book to database.
     *
     * @param book Book to add
     */
    public void addBook(Book book) {
        _books.save(book);
    }

    /**
     * Removes a book from database.
     *
     * @param isbn ISBN of the book to delete
     */
    public void deleteBook(String isbn) {
        _books.deleteById(isbn);
    }

    /**
     * Updates a book in database.
     *
     * @param isbn ISBN of the book to update
     * @param book Updated book
     */
    public void updateBook(String isbn, Book book)
    {
        book.isbn = isbn;
        _books.save(book);
    }

    /**
     * Imports books from a JSON or CSV file to add to database.
     *
     * @param fileName Name of the file to import the books from
     * @throws FileNotFoundException if the fileName does not point to a valid file to import from.
     */
    public void importBooks(String fileName) throws FileNotFoundException {

        File file = new File(fileName);

        // Act based on MIME type
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (Objects.equals(mimeType, "text/csv"))
        {
            try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
                List<String[]> csvData = csvReader.readAll();
                List<Book> books = new ArrayList<>();

                // Format: title:author:isbn:publisher:genre:price
                for (int i = 1; i < csvData.size(); i++) { // Note that it skips the header row.
                    String[] row = csvData.get(i);

                    if (row.length == 7) {
                        Book book = new Book();
                        book.title = row[0];
                        book.author = row[1];
                        book.isbn = row[2];
                        book.publisher = row[3];
                        book.genre = row[4];
                        book.price = Double.parseDouble(row[5]);

                        books.add(book);
                    }
                }

                _books.saveAll(books);

            } catch (IOException | CsvException e) {
                e.printStackTrace();
                throw new RuntimeException("Error processing CSV file");
            }
        } else if (Objects.equals(mimeType, "application/json"))
        {
            StringBuilder contents = new StringBuilder();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                contents.append(scanner.nextLine());
            }
            scanner.close();
            Gson gson = new Gson();

            List<Book> books = gson.fromJson(contents.toString(), new TypeToken<List<Book>>(){}.getType());

            _books.saveAll(books);
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * Exports given books in CSV format.
     *
     * @param books Books to export.
     * @return The export of the books in a string of CSV format.
     */
    public String exportCSV(Iterable<Book> books) {
        StringBuilder builder = new StringBuilder();

        // Header row
        builder.append("title,author,isbn,publisher,genre,price\n");
        for (Book book : books) {
            builder.append(book.toCSV()).append("\n");
        }

        return builder.toString();
    }

    /**
     * Exports given books in JSON format.
     *
     * @param books Books to export.
     * @return The export of the books in a string of JSON format.
     */
    public String exportJSON(Iterable<Book> books) {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n"); // Start JSON array

        boolean first = true; // Little trick to have proper comma placement without trailing comma
        for (Book book : books) {
            if (!first) {
                builder.append(",\n"); // We place the comma before each linebreak exc. the first iteration.
            }
            builder.append(book.toJSON());
            first = false;
        }

        builder.append("\n]\n"); // End JSON array with linebreak on end.
        return builder.toString();
    }


}
