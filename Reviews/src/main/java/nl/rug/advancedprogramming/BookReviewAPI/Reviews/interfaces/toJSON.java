package nl.rug.advancedprogramming.BookReviewAPI.Reviews.interfaces;

/**
 * Interface that provides a method to convert an object to a JSON (JavaScript Object Notation) format.
 * Classes implementing this interface should define how their instances are represented as a JSON string.
 */
public interface toJSON {

    /**
     * Converts the implementing object to a JSON formatted string.
     *
     * @return A string representing the object in JSON format.
     */
    String toJSON();
}
