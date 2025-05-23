
# Specification

- Parameters and types are specified in the class diagram

## Controler

### Description

- this class is responsible for the HTTP connection and the endpoints
- it has almost the same function names as the Service Class, but it's function only establish the connection to the endpoints and hand the logic part to the equivilent function in the Manager class

### functions/endpoints
- getReview(), endpoint: GET /books/{bookId}/reviews
- addReview(), endpoint: POST /books/{bookId}/reviews
- updateReview(), endpoint: PUT /books/{bookID}/reviews{reviewID}
- deleteReview(), endpoint: DELETE /books{bookID}/reviews{reviewID}
- importReviews(), endpoint: POST /reviews/import
- exportReviews(), endpoint: GET /reviews/export


## ReviewManager

### Description

- This class handles the logic part, it gets requests from the controler class and interacts with the repository by giving requests concernign the Database to it. In the end it gives the result back to the controler class

### functions

- addReview(): adds review to repository
- getReview(): reads reveiw out of repository
- updateReview(): updates review in repository
- deleteReview(): deletes review in repository
- getAllReviews(): gets all reviews of a specific bookId
- calculateAverageRating(): calculates average of all reviews to a given bookId


## DBConnector

### Description

- This class manages the Database and executes requests from the Service class. It is responsible for CRUD operations on the Database.

### functions

- readReview(): looks for review by reviewId
- massReadReaview(): used for import
- updateReview(): updates existing Review by reviewId
- saveReview(): saves review Object
- massSaveReview(): used for export
- deleteReview(): deletes review out of database


## Review

### Description

- this class shows what review objects are

### Attributes

- reviewId: unique for each review
- bookId: assigns review to a book
- rating: rating between 1 and 5
- comment: written content of the review
- reviewDate: date of creation of the review

### Functions

- Review(): constructor
- getId(): returns Id
- getBookId(): returns BookId
- getRating(): returns Rating
- getComment(): returns Comment
- getReviewDate(): returns date
- setRating(): changes rating
- setComment(): changes comment
- toCSV(): creates CSV
- toJSON(): creates JSON


# Interface JSON

## Description

- used for exporting in JSON format

## Functions

- toJSON(): function for exporting in JSON

# Interface CSV

## Description

- used for exporting in CSV format

## Functions

- toCSV(): used for exporting in CSV