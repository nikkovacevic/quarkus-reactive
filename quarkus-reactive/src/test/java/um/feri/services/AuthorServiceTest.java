package um.feri.services;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import um.feri.model.Author;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@RunOnVertxContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorServiceTest {

    @Inject
    AuthorService service;

    private static Long authorId;

    @Test
    @Order(1)
    public void addAuthor(TransactionalUniAsserter a) {
        Author author = new Author("J. K.", "Rowling", LocalDate.of(1965, 7, 31));
        a.assertThat(() -> service.addAuthor(author), newAuthor -> {
            authorId = newAuthor.id;
            assertNotNull(newAuthor);
            assertEquals("J. K.", newAuthor.getName());
            assertEquals("Rowling", newAuthor.getSurname());
        });
        a.assertEquals(() -> Author.count(), 1L);

        Author existingAuthor = new Author("J. K.", "Rowling", LocalDate.of(1965, 7, 31));
        a.assertThat(() -> service.addAuthor(existingAuthor), newAuthor -> {
            assertNotNull(newAuthor);
            assertEquals("J. K.", newAuthor.getName());
            assertEquals("Rowling", newAuthor.getSurname());
        });
        a.assertEquals(() -> Author.count(), 1L);
    }

    @Test
    @Order(2)
    public void getAllAuthors(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getAllAuthors(), authors -> {
            assertNotNull(authors);
            assertEquals(1, authors.size());
        });
    }

    @Test
    @Order(3)
    public void getAuthorById(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getAuthorById(authorId), response -> {
            assertEquals(200, response.getStatus());
            Author author = (Author) response.getEntity();
            assertNotNull(author);
            assertEquals("J. K.", author.getName());
            assertEquals("Rowling", author.getSurname());
        });

        a.assertThat(() -> service.getAuthorById(99L), response -> {
            assertEquals(404, response.getStatus());
            Author author = (Author) response.getEntity();
            assertNull(author);
        });
    }

    @Test
    @Order(4)
    public void findIfAuthorExists(TransactionalUniAsserter a) {
        Author author = new Author("J. K.", "Rowling", LocalDate.of(1965, 7, 31));
        a.assertThat(() -> service.findIfAuthorExists(author), existingAuthor -> {
            assertNotNull(existingAuthor);
            assertEquals("J. K.", existingAuthor.getName());
            assertEquals("Rowling", existingAuthor.getSurname());
        });
    }

    @Test
    @Order(5)
    public void updateAuthor(TransactionalUniAsserter a) {
        Author author = new Author("J.", "K. Rowling", LocalDate.of(1965, 7, 31));
        a.assertThat(() -> service.updateAuthor(authorId, author), response -> {
            assertEquals(200, response.getStatus());
            Author updatedAuthor = (Author) response.getEntity();
            assertNotNull(updatedAuthor);
            assertEquals("J.", updatedAuthor.getName());
            assertEquals("K. Rowling", updatedAuthor.getSurname());
        });

        a.assertThat(() -> service.updateAuthor(99L, author), response -> {
            assertEquals(404, response.getStatus());
            Author updatedAuthor = (Author) response.getEntity();
            assertNull(updatedAuthor);
        });
    }

    @Test
    @Order(6)
    public void deleteAuthor(TransactionalUniAsserter a) {
        a.assertThat(() -> service.deleteAuthor(authorId), response -> {
            assertEquals(204, response.getStatus());
        });
        a.assertEquals(() -> Author.count(), 0L);

        a.assertThat(() -> service.deleteAuthor(99L), response -> {
            assertEquals(404, response.getStatus());
        });
    }


}