package um.feri.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorResourceTest {

    private static String authorId;

    @Test
    @Order(1)
    public void createAuthor() {
        Integer createdId = given()
                .contentType("application/json")
                .body("{ \"name\": \"George\", \"surname\": \"Orwell\", \"dateOfBirth\": \"1903-06-25\" }")
                .when()
                .post("/authors")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
        authorId = createdId.toString();
    }

    @Test
    @Order(2)
    public void listAllAuthors() {
        given()
                .when()
                .get("/authors")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", equalTo(1));
    }

    @Test
    @Order(2)
    public void getAuthorById() {
        given()
                .when()
                .get("/authors/1")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("George"))
                .body("surname", equalTo("Orwell"));

        given()
                .when()
                .get("/authors/99")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    public void updateAuthor() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"George\", \"surname\": \"Orwell\", \"dateOfBirth\": \"1902-06-25\" }")
                .when()
                .put("/authors/" + authorId)
                .then()
                .statusCode(200)
                .body("dateOfBirth", equalTo("1902-06-25"));

    }

    @Test
    @Order(5)
    public void deleteAuthor() {
        given()
                .when()
                .delete("/authors/" + authorId)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/authors/" + authorId)
                .then()
                .statusCode(404);

    }
}