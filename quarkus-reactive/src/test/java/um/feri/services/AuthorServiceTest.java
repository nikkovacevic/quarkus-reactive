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

    @Test
    @RunOnVertxContext
    @Order(1)
    public void getAllAuthors(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getAllAuthors(), authors -> {
            assertNotNull(authors);
            assertEquals(5, authors.size());
        });
    }

    @Test
    @RunOnVertxContext
    @Order(2)
    public void getAuthorById(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getAuthorById(1L), response -> {
            assertEquals(200, response.getStatus());
            Author author = (Author) response.getEntity();
            assertNotNull(author);
            assertEquals("Jane", author.getName());
            assertEquals("Austen", author.getSurname());
        });

        a.assertThat(() -> service.getAuthorById(99L), response -> {
            assertEquals(404, response.getStatus());
            Author author = (Author) response.getEntity();
            assertNull(author);
        });
    }

    @Test
    @RunOnVertxContext
    @Order(3)
    public void findIfAuthorExists(TransactionalUniAsserter a) {
        Author author = new Author("George", "Orwell", LocalDate.of(1903, 6, 25));
        a.assertThat(() -> service.findIfAuthorExists(author), existingAuthor -> {
            assertNotNull(existingAuthor);
            assertEquals("George", existingAuthor.getName());
            assertEquals("Orwell", existingAuthor.getSurname());
        });
    }

    @Test
    @RunOnVertxContext
    @Order(4)
    public void addAuthor(TransactionalUniAsserter a) {
        Author author = new Author("J. K.", "Rowling", LocalDate.of(1965, 7, 31));
        a.assertThat(() -> service.addAuthor(author), newAuthor -> {
            assertNotNull(newAuthor);
            assertEquals("J. K.", newAuthor.getName());
            assertEquals("Rowling", newAuthor.getSurname());
        });
        a.assertEquals(() -> Author.count(), 6L);

        Author existingAuthor = new Author("George", "Orwell", LocalDate.of(1903, 6, 25));
        a.assertThat(() -> service.addAuthor(existingAuthor), newAuthor -> {
            assertNotNull(newAuthor);
            assertEquals("George", newAuthor.getName());
            assertEquals("Orwell", newAuthor.getSurname());
        });
        a.assertEquals(() -> Author.count(), 6L);
    }

    @Test
    @RunOnVertxContext
    @Order(5)
    public void updateAuthor(TransactionalUniAsserter a) {
        Author author = new Author("John", "Orwell", LocalDate.of(1903, 6, 25));
        a.assertThat(() -> service.updateAuthor(3L, author), response -> {
            assertEquals(200, response.getStatus());
            Author updatedAuthor = (Author) response.getEntity();
            assertNotNull(updatedAuthor);
            assertEquals("John", updatedAuthor.getName());
            assertEquals("Orwell", updatedAuthor.getSurname());
        });

        a.assertThat(() -> service.updateAuthor(99L, author), response -> {
            assertEquals(404, response.getStatus());
            Author updatedAuthor = (Author) response.getEntity();
            assertNull(updatedAuthor);
        });
    }

    @Test
    @RunOnVertxContext
    @Order(6)
    public void deleteAuthor(TransactionalUniAsserter a) {
        a.assertThat(() -> service.deleteAuthor(1L), response -> {
            assertEquals(204, response.getStatus());
        });
        a.assertEquals(() -> Author.count(), 5L);

        a.assertThat(() -> service.deleteAuthor(99L), response -> {
            assertEquals(404, response.getStatus());
        });
    }


}