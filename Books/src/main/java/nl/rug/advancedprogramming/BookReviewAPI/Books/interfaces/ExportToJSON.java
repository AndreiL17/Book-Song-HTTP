package nl.rug.advancedprogramming.BookReviewAPI.Books.interfaces;

public interface ExportToJSON {
    /**
     * Serialize the object of the class to a CSV string.
     *
     * @return the object of the class in CSV format.
     */
    String toJSON();
}
