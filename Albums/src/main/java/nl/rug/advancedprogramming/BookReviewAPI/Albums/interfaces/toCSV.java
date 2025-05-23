package nl.rug.advancedprogramming.BookReviewAPI.Albums.interfaces;

/**
 * Interface for objects that can be converted to a CSV (Comma-Separated Values) format.
 */
public interface toCSV {

    /**
     * Converts the implementing object to a CSV-formatted string.
     *
     * @return a CSV representation of the object.
     */
    String toCSV();
}
