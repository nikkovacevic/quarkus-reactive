package um.feri.services;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import um.feri.model.Author;
import um.feri.model.Book;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@RunOnVertxContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceTest {

    @Inject
    BookService service;

    @Test
    @RunOnVertxContext
    @Order(1)
    void createBook(TransactionalUniAsserter a) {
        Author author = new Author("Agatha", "Christie", LocalDate.of(1809, 9, 15));
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, author);

        a.assertThat(() -> service.createBook(book), response -> {
            assertEquals(201, response.getStatus());
            Book newBook = response.getEntity();
            assertNotNull(newBook);
            assertEquals("Murder on the Orient Express", newBook.getTitle());
            assertEquals("1234567890", newBook.getIsbn());
            assertEquals(39.99, newBook.getPrice());
        });
        a.assertEquals(() -> Book.count(), 1L);
        a.assertEquals(() -> Author.count(), 1L);

        Book existingBook = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, author);
        a.assertThat(() -> service.createBook(existingBook), response -> {
            assertEquals(409, response.getStatus());
            Book conflictBook = response.getEntity();
            assertNotNull(conflictBook);
            assertEquals("Murder on the Orient Express", conflictBook.getTitle());
        });

        a.assertEquals(() -> Book.count(), 1L);

        Author newAuthor = new Author("J. R. R.", "Tolkien", LocalDate.of(1892, 1, 3));
        Book bookWithNewAuthor = new Book("Lord of the Rings", "123456789", 39.99, 1954, newAuthor);
        a.assertThat(() -> service.createBook(bookWithNewAuthor), response -> {
            assertEquals(201, response.getStatus());
            Book newBook = response.getEntity();
            assertNotNull(newBook);
            assertEquals("Lord of the Rings", newBook.getTitle());
            assertEquals("123456789", newBook.getIsbn());
            assertEquals(39.99, newBook.getPrice());
        });

        a.assertEquals(() -> Book.count(), 2L);
        a.assertEquals(() -> Author.count(), 2L);
    }


    @Test
    @RunOnVertxContext
    @Order(2)
    void getAllBooks(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getAllBooks(), books -> {
            assertNotNull(books);
            assertEquals(2, books.size());
        });
    }

    @Test
    @RunOnVertxContext
    @Order(3)
    void getBookById(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getBookById(1L), response -> {
            assertEquals(200, response.getStatus());
            Book book = (Book) response.getEntity();
            assertNotNull(book);
            assertEquals("Murder on the Orient Express", book.getTitle());
            assertEquals("Christie", book.getAuthor().getSurname());
        });

        a.assertThat(() -> service.getBookById(99L), response -> {
            assertEquals(404, response.getStatus());
            Book book = (Book) response.getEntity();
            assertNull(book);
        });
    }

    @Test
    @RunOnVertxContext
    @Order(4)
    void findIfBookExists(TransactionalUniAsserter a) {
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, null);
        a.assertThat(() -> service.findIfBookExists(book), existingBook -> {
            assertNotNull(existingBook);
            assertEquals("Murder on the Orient Express", existingBook.getTitle());
            assertEquals(1934, existingBook.getYearOfRelease());
        });

    }


    @Test
    @RunOnVertxContext
    @Order(5)
    void updateBook(TransactionalUniAsserter a) {
        Book book = new Book("Murder on the Orient Express", "1234567890", 29.99, 1934, null);
        a.assertThat(() -> service.updateBook(1L, book), response -> {
            assertEquals(200, response.getStatus());
            Book updatedBook = (Book) response.getEntity();
            assertNotNull(updatedBook);
            assertEquals(29.99, updatedBook.getPrice());
        });

        a.assertThat(() -> service.updateBook(99L, book), response -> {
            assertEquals(404, response.getStatus());
            Book updatedBook = (Book) response.getEntity();
            assertNull(updatedBook);
        });
    }

    @Test
    @RunOnVertxContext
    @Order(6)
    void deleteBook(TransactionalUniAsserter a) {
        a.assertThat(() -> service.deleteBook(1L), response -> {
            assertEquals(204, response.getStatus());
        });
        a.assertEquals(() -> Book.count(), 1L);

        a.assertThat(() -> service.deleteBook(99L), response -> {
            assertEquals(404, response.getStatus());
        });
    }
}