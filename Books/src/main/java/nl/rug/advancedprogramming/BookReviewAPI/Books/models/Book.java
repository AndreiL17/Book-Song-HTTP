package nl.rug.advancedprogramming.BookReviewAPI.Books.models;

import com.google.gson.Gson;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import nl.rug.advancedprogramming.BookReviewAPI.Books.interfaces.ExportToCSV;
import nl.rug.advancedprogramming.BookReviewAPI.Books.interfaces.ExportToJSON;

@Setter
@Getter
@Entity
public class Book implements ExportToCSV, ExportToJSON {

    public String title;
    public String author;
    public String publisher;
    @Id
    public String isbn;
    public String genre;
    public double price;

    // Constructor with parameters
    public Book(String title, String author, String publisher, String isbn, String genre, double price) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.genre = genre;
        this.price = price;
    }

    // Default constructor. Not in spec but needed by the framework.
    public Book() {
    }

    @Override
    public String toCSV() {
        // javadoc is on the interface that this overrides
        return String.join(",",
                title, author, isbn, publisher, genre, String.valueOf(price)
        );
    }

    @Override
    public String toJSON() {
        // javadoc is on the interface that this overrides
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
