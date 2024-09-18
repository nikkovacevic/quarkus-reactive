package um.feri.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import um.feri.model.Author;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorServiceTest {

    @Inject
    AuthorService service;

    private static Long authorId;

    @Test
    @Order(1)
    void addAuthor() {
        Author author = new Author("J. K.", "Rowling", LocalDate.of(1965, 7, 31));
        Author newAuthor = service.addAuthor(author);
        authorId = newAuthor.id;
        assertNotNull(newAuthor);
        assertEquals("J. K.", newAuthor.getName());
        assertEquals("Rowling", newAuthor.getSurname());
        assertEquals(1, Author.count());

        Author existingAuthor = new Author("J. K.", "Rowling", LocalDate.of(1965, 7, 31));
        newAuthor = service.addAuthor(existingAuthor);
        assertNotNull(newAuthor);
        assertEquals("J. K.", newAuthor.getName());
        assertEquals("Rowling", newAuthor.getSurname());
        assertEquals(1, Author.count());
    }

    @Test
    @Order(2)
    void getAllAuthors() {
        List<Author> authors = service.getAllAuthors();
        assertNotNull(authors);
        assertEquals(1, authors.size());
    }

    @Test
    @Order(3)
    void getAuthorById() {
        Author author = service.getAuthorById(authorId);
        assertNotNull(author);
        assertEquals("J. K.", author.getName());
        assertEquals("Rowling", author.getSurname());

        assertThrows(NotFoundException.class, () -> service.getAuthorById(99L));
    }

    @Test
    @Order(4)
    void findIfAuthorExists() {
        Author existingAuthor = new Author("J. K.", "Rowling", LocalDate.of(1965, 7, 31));
        Author author = service.findIfAuthorExists(existingAuthor);
        assertNotNull(author);
        assertEquals("J. K.", author.getName());
        assertEquals("Rowling", author.getSurname());
    }

    @Test
    @Order(5)
    void updateAuthor() {
        Author author = new Author("J.", "K. Rowling", LocalDate.of(1965, 7, 31));
        Response response = service.updateAuthor(authorId, author);
        Author updatedAuthor = (Author) response.getEntity();
        assertEquals(200, response.getStatus());
        assertNotNull(updatedAuthor);
        assertEquals("J.", updatedAuthor.getName());
        assertEquals("K. Rowling", updatedAuthor.getSurname());

        assertThrows(NotFoundException.class, () -> service.updateAuthor(99L, author));
    }

    @Test
    @Order(6)
    void deleteAuthor() {
        Response response = service.deleteAuthor(authorId);
        assertEquals(204, response.getStatus());


        assertThrows(NotFoundException.class, () -> service.deleteAuthor(99L));
    }
}