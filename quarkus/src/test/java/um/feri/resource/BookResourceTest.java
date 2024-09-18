package um.feri.resource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import um.feri.model.Book;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class BookResourceTest {

    private static Long bookId;

    @Transactional
    @BeforeEach
    void setUp() {
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        book.persist();
        bookId = book.id;
    }

    @Transactional
    @AfterEach
    void tearDown() {
        Book.deleteAll();
    }

    @Test
    void getBooks() {
        given()
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", equalTo(1));
    }

    @Test
    void getBookById() {
        given()
                .when()
                .get("/books/" + bookId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
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
                .contentType("application/json")
                .body(newBookJson)
                .when()
                .post("/books")
                .then()
                .statusCode(201);

        String newBookWithDuplicateIsbn = "{ \"title\": \"The Hobbit\", \"isbn\": \"9876543210\", \"price\": 49.99, \"yearOfRelease\": 1937, \"author\": \"J.R.R. Tolkien\" }";
        given()
                .contentType("application/json")
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
                .contentType("application/json")
                .body(updatedBookJson)
                .when()
                .put("/books/" + bookId)
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
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