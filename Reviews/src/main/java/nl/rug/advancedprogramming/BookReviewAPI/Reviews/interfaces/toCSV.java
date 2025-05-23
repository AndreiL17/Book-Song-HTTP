package nl.rug.advancedprogramming.BookReviewAPI.Reviews.interfaces;

/**
 * Interface that provides a method to convert an object to a CSV (Comma-Separated Values) format.
 * Classes implementing this interface should define how their instances are represented as a CSV string.
 */
public interface toCSV {

    /**
     * Converts the implementing object to a CSV formatted string.
     *
     * @return A string representing the object in CSV format.
     */
    String toCSV();
}
