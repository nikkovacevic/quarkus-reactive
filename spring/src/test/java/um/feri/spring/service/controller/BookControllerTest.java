package um.feri.spring.service.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import um.feri.spring.model.Book;
import um.feri.spring.repository.BookRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private BookRepository bookRepository;

    private static Long bookId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        bookRepository.save(book);
        bookId = book.getId();
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void getBooks() {
        given()
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("size()", equalTo(1));
    }

    @Test
    void getBookById() {
        given()
                .when()
                .get("/books/" + bookId)
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("title", equalTo("Murder on the Orient Express"))
                .body("isbn", equalTo("1234567890"))
                .body("price", equalTo(39.99F))
                .body("yearOfRelease", equalTo(1934))
                .body("author", equalTo("Agatha Christie"));

        given()
                .when()
                .get("/books/99")
                .then()
                .statusCode(404);
    }

    @Test
    void addBook() {
        String newBookJson = "{ \"title\": \"The Hobbit\", \"isbn\": \"9876543210\", \"price\": 49.99, \"yearOfRelease\": 1937, \"author\": \"J.R.R. Tolkien\" }";
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(newBookJson)
                .when()
                .post("/books")
                .then()
                .statusCode(201);

        String newBookWithDuplicateIsbn = "{ \"title\": \"The Hobbit\", \"isbn\": \"9876543210\", \"price\": 49.99, \"yearOfRelease\": 1937, \"author\": \"J.R.R. Tolkien\" }";
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(newBookWithDuplicateIsbn)
                .when()
                .post("/books")
                .then()
                .statusCode(409);
    }

    @Test
    void updateBook() {
        String updatedBookJson = "{ \"title\": \"Murder on the Orient Express\", \"isbn\": \"1234567890\", \"price\": 29.99, \"yearOfRelease\": 1934, \"author\": \"Agatha Christie\" }";
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updatedBookJson)
                .when()
                .put("/books/" + bookId)
                .then()
                .statusCode(204);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updatedBookJson)
                .when()
                .put("/books/99")
                .then()
                .statusCode(404);
    }

    @Test
    void deleteBook() {
        given()
                .when()
                .delete("/books/" + bookId)
                .then()
                .statusCode(204);

        given()
                .when()
                .delete("/books/99")
                .then()
                .statusCode(404);
    }
}