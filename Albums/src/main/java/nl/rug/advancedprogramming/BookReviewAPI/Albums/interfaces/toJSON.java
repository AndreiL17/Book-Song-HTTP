package nl.rug.advancedprogramming.BookReviewAPI.Albums.interfaces;

/**
 * Interface for objects that can be converted to a JSON (JavaScript Object Notation) format.
 */
public interface toJSON {

    /**
     * Converts the implementing object to a JSON-formatted string.
     *
     * @return a JSON representation of the object.
     */
    String toJSON();
}
