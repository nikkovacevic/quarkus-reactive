package um.feri.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorResourceTest {

    private static String authorId;

    @Test
    @Order(1)
    public void listAllAuthors() {
        given()
                .when()
                .get("/authors")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", equalTo(5));
    }

    @Test
    @Order(2)
    public void getAuthorById() {
        given()
                .when()
                .get("/authors/3")
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
    @Order(3)
    public void createAuthor() {
        Integer createdId = given()
                .contentType("application/json")
                .body("{ \"name\": \"Nik\", \"surname\": \"Kovacevic\", \"dateOfBirth\": \"2000-10-03\" }")
                .when()
                .post("/authors")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
        authorId = createdId.toString();
    }
    @Test
    @Order(4)
    public void updateAuthor() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"Nik\", \"surname\": \"Kovacevic\", \"dateOfBirth\": \"1990-01-01\" }")
                .when()
                .put("/authors/" + authorId)
                .then()
                .statusCode(200)
                .body("dateOfBirth", equalTo("1990-01-01"));

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