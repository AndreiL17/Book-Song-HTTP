# Module A Specification

Below is the UML diagram of the specification. Under it is the UML code in case problems with viewing the diagram.
After that, a breakdown of each class we specified, along with descriptions of itself and its methods.
The second section comprises the specification of the API endpoints corresponding to the methods of BookController.
The last section then outlines and describes the design patterns applicable to our specification.
---
![Class Diagram](diagram/ClassDiagram%20%281%29.png)

### UML code

```
@startuml
skinparam padding 3
left to right direction
package specify {
  class Main{
    + main(String[]): void
  }
  
  class Book {
    +Book(String, String, String, String, String, double)
    - String title
    - String author
    - String publisher
    - String isbn
    - String genre
    - double price
    + getTitle(): String
    + getAuthor(): String
    + getPublisher(): String
    + getIsbn(): String
    + getGenre(): String
    + getPrice(): double
    + setTitle(String): void
    + setAuthor(String): void
    + setPublisher(String): void
    + setIsbn(String): void
    + setGenre(String): void
    + setPrice(double): void
  }
  
  package controller{
    class BookController{
      - BookService bookService
      + BookController(BookService)
      + addBook(Book): void
      + getAllBooks(): Iterable<Book>
      + updateBook(int, Book): void
      + deleteBook(int): void
      + getByProperty(String, String): Iterable<Book>
      + importBooks(String): void
      + export(String, Iterable<Books>): void
    }
  }
  
  package service{
    class BookService{
      + BookService()
      + addBook(Book): void
      + getAllBooks(): Iterable<Book>
      + updateBook(int, Book): void
      + deleteBook(int): void
      + getByProperty(String, String): Iterable<Book>
      + importBooks(String): void
      + exportJSON(Iterable<Books>): void
      + exportCSV(Iterable<Books>): void
    }
  }
  
  package repository{
    interface BookRepository{
      +getByTitle(String): Iterable<Book>
      +getByAuthor(String): Iterable<Book>
      +getByPublisher(String): Iterable<Book>
      +getByIsbn(String): Iterable<Book>
      +getByGenre(String): Iterable<Book>
      +getByPrice(double): Iterable<Book>
    }
  }
  
  package Interfaces{
    interface ExportToJSON{
      + toJSON(): String
    }
    interface ExportToCSV{
      + toCSV(): String
    }
  }
  Book ..|> ExportToJSON
  Book ..|> ExportToCSV
  BookController "1" *--> "bookService" BookService
  BookService "1" *--> "bookRepository" BookRepository
  BookController ..> Book
  BookService ..> Book
  BookRepository ..> Book

}
@enduml
```
---
# Class Descriptions

## Book Class

- **Class Name**: `Book`


- **Description**: Represents a book with various attributes like title, author, etc.


- **Fields**:

  - `String title`: The title of the book.

  - `String author`: The author of the book.

  - `String publisher`: The publisher of the book.

  - `String isbn`: The ISBN of the book.

  - `String genre`: The genre of the book.

  - `double price`: The price of the book.


- **Methods**:

  This class's only methods are getters and setters which are already self-explanatory.

---

## BookController Class

- **Class Name**: `BookController`


- **Description**: Handles API requests for managing books. It forwards requests relating the business logic
 of the program to the service layer.


- **Fields**:

  - `BookService bookService`: The service layer for book-related operations.


- **Methods**:

  - `addBook(Book)`: Adds a new book.

  - `getAllBooks()`: Returns all books.

  - `updateBook(int isbn, Book)`: Updates a book by its ISBN.

  - `deleteBook(int isbn)`: Deletes a book by its ISBN.

  - `getByProperty(String property, String value)`: Returns books based on a specific property (e.g., author, title).

  - `importBooks(String)`: Imports books from a given file.

  - `export(String format, Iterable<Book>)`: Exports books to the specified format (JSON or CSV).

---

## BookService Class

- **Class Name**: `BookService`


- **Description**: Contains business logic for managing books. It serves as a middle ground between BookRepository and BookController, ensuring that the controller communicates
with the database as wanted by the user.


- **Methods**:

  - `addBook(Book)`: Adds a new book to the repository.

  - `getAllBooks()`: Retrieves all books from the repository.

  - `updateBook(int isbn, Book)`: Updates an existing book by its ISBN.

  - `+ deleteBook(int isbn): void`: Deletes a book by its ISBn.

  - `getByProperty(String property, String value)`: Retrieves books based on a property (e.g., author, title, ISBN).

  - `+ importBooks(String)`: Imports books from a file.

  - `exportJSON(Iterable<Book>)`: Exports books from given collection into a JSON format.

  - `exportCSV(Iterable<Book>)`: Exports books from collection in a CSV format.

---
## BookRepository Interface

- **Interface Name**: `BookRepository`


- **Description**: Interface for accessing book data from the data source. It also extends CrudRepository for CRUD methods not specified below.


- **Methods**:

  - `getByTitle(String)`: Retrieves books by specified title.

  - `getByAuthor(String)`: Retrieves books by specified author.

  - `getByPublisher(String)`: Retrieves books by specified publisher.

  - `getByIsbn(String)`: Retrieves books by specified ISBN.

  - `getByGenre(String)`: Retrieves books by specified genre.

  - `getByPrice(double)`: Retrieves books by specified price.

---

## ExportToJSON Interface

- **Interface Name**: `ExportToJSON`

- **Description**: Interface for exporting data to JSON format.

- **Methods**:

  - `toJSON()`: Exports the object to a JSON string.
---

## ExportToCSV Interface

- **Interface Name**: `ExportToCSV`

- **Description**: Interface for exporting data to CSV format.

- **Methods**:

  - `toCSV()`: Exports the object to a CSV string.
---

# BookController API Endpoints

The `BookRepository` interface provides several methods to interact with books in the daabase. Below is a breakdown of each method and its purpose.

### 1. `Create Book`
- **Endpoint**: `POST /api/books{Book}`
- **Description**: Adds a new book to the system.
- **Parameter**: `@RequestBody Book`: a book object to be added to database
- **Response**: 
  - `201 Created` on success, also created book data
  - `422 Unprocessable Entity` on failure
- **Request Body**:
  - ```json
      [
        {
          "title": "string",
          "author": "string",
          "isbn": "string",
          "publisher": "string",
          "genre": "string",
          "price": "double"
        }
      ]
      ```

### 2. `Read All Books`
- **Endpoint**: `GET /api/books`
- **Description**: Retrieves all books from database
- **Parameters**: None
- **Response**:
    - `200 OK `on success
    - `500 Internal Server Failure` if fails

### 3. `Update Book`
- **Endpoint**: `PUT /api/books/{isbn}`
- **Description**: Updates the information of an existing `Book`.
- **Parameters**:
  - `@PathVariable int isbn`: isbn of book to be changed
  - `@RequestBody Book`: the updated version of the book
- **Response**: 
  - `200 OK`
  - `404 Not Found` if the book to be updated is not in database
  - `400 Bad Request` if invalid parameters

### 4. `Delete Book`
- **Endpoint**: `DELETE /api/books/{isbn}`
- **Description**: Deletes a book from the system by its ISBN.
- **Parameters**: `@PathVariable int isbn`: isbn of book wanting to be deleted
- **Response**:
  - `204 No Content` on success
  - `404 Not Found` when book not found in database
  - `400 Bad Request` if invalid parameters

### 5. `Retrieve Books By Property`
- **Endpoint**: `GET /api/books?property={property}&value={value}`
- **Description**: Retrieves all books based on the specified value of a wanted attribute.
- **Parameters**: 
  - `@RequestParam String property`: property which we want to retrieve by
  - `@RequetParam String value`: value of the property
- **Response**: 
  - `200 OK` on success
  - `404 Not Found` if no books have specified attribute
  - `400 Bad Request` if parameters invalid

### 6. `Import Books(JSON or CSV)`
- **Endpoint**: `POST /api/books/import`
- **Description**: Imports books. Supports JSON and CSV which is handled by BookService.
- **Parameters**: `@RequestParam String filePath`: the path to the file with data to import
- **Response**: 
  - `201 Created`
  - `400 Bad Request`: if parameters are invalid
  - `500 Internal Server Error`: if import process yields problems

### 7. `Export Books(JSON or CSV)`
- **Endpoint**: `GET /api/books/export`
- **Description**: Exports all books in bulk in the requested format (JSON or CSV).
- **Parameters**:
    - `@RequestBody Iterable<Book>`: list of books to export
    - `@RequestParam String format`: either JSON or CSV
- **Request Body**:
  - ```json
      [
        {
          "title": "string",
          "author": "string",
          "isbn": "string",
          "publisher": "string",
          "genre": "string",
          "price": "double"
        }
      ]
      ```
- **Response**:
  -  `200 OK`
  -  `400 Bad Request` if invalid parameters
  - `500 Internal Server Error` if there is an issue with exporting
---
# Design Patterns
### 1. Service Layer Pattern
The `Controller` layer is modelled by the `BookController` class, and it receives and processes
incoming requests from the user interface. It then passes these requests to the service layer 
for logic operations. The `BookService` class represents the `Service` layer. It encapsulates all the
business logic related to books. It handles operations specified by the user through the `BookRepository` interface.
The `Repository` layer is tasked with interacting directly with the database. It provides abstraction for
the data access layer, allowing `Service` to perform operations on data in the system without giving it 
direct details about the storage of the data.

### 2. Repository Pattern
The repository pattern is used to abstract the data layer, restricting the business logic from the underlying data source.
The `BookRepository` interface defines methods for accessing books by various properties (getByTitle(), getByAuthor(), etc.). 
`BookService` interacts with the repository interface, ensuring separation of concerns between business logic and data access.

### 3. Strategy Pattern
The strategy pattern allows for defining a family of algorithms and making them interchangeable. 
It can be used to switch between different strategies at runtime. The export functionality in `BookService` 
uses two strategies for exporting data: exportJSON() and exportCSV(). `Book` implements both export variants,
allowing the user to choose which format they wish to export to through user input.

### 4. Dependency Injection
This pattern is used to provide dependencies to a class from external sources, making the code 
more flexible and easier to test. Through constructor injection seen in the `BookController` class, decoupling
the usage of the `BookService` from its creation.



