package um.feri.services;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import um.feri.model.Book;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@RunOnVertxContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceTest {

    @Inject
    BookService service;

    private static Long bookId;

    @Transactional
    @BeforeEach
    void setUp(TransactionalUniAsserter a) {
        a.execute(() -> {
            Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
            return book.persist()
                    .onItem().transform(persisted -> {
                        bookId = book.id;
                        return book;
                    });
        });
    }

    @Transactional
    @AfterEach
    void tearDown(TransactionalUniAsserter a) {
        a.execute(() -> Book.deleteAll());
    }

    @Test
    void getAllBooks(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getAllBooks(), books -> {
            assertEquals(1, books.size());
        });
    }

    @Test
    void getBookById(TransactionalUniAsserter a) {
        a.assertThat(() -> service.getBookById(bookId), book -> {
            assertEquals("Murder on the Orient Express", book.getTitle());
            assertEquals("1234567890", book.getIsbn());
            assertEquals(39.99, book.getPrice());
            assertEquals(1934, book.getYearOfRelease());
            assertEquals("Agatha Christie", book.getAuthor());
        });

        a.assertFailedWith(() -> service.getBookById(99L), NotFoundException.class);
    }

    @Test
    void findIfBookExists(TransactionalUniAsserter a) {
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        a.assertThat(() -> service.findBookIfExists(book), existingBook -> {
            assertEquals("Murder on the Orient Express", existingBook.getTitle());
            assertEquals("1234567890", existingBook.getIsbn());
            assertEquals(39.99, existingBook.getPrice());
            assertEquals(1934, existingBook.getYearOfRelease());
            assertEquals("Agatha Christie", existingBook.getAuthor());
        });
    }

    @Test
    void createBook(TransactionalUniAsserter a) {
        Book book = new Book("Lord of the Rings", "123456789", 39.99, 1954, "J. R. R. Tolkien");
        a.assertThat(() -> service.createBook(book), newBook -> {
            assertNotNull(newBook.id);
        });
        a.assertEquals(() -> Book.count(), 2L);

        Book existingBook = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        a.assertFailedWith(() -> service.createBook(existingBook), WebApplicationException.class);
    }


    @Test
    void updateBook(TransactionalUniAsserter a) {
        Book book = new Book("Murder on the Orient Express", "1234567890", 29.99, 1934, "Agatha Christie");
        a.execute(() -> service.updateBook(bookId, book))
            .assertThat(() -> service.getBookById(bookId), updatedBook -> {
                assertEquals(29.99, updatedBook.getPrice());
            });

        a.assertFailedWith(() -> service.updateBook(99L, book), NotFoundException.class);
    }

    @Test
    void deleteBook(TransactionalUniAsserter a) {
        a.execute(() -> service.deleteBook(bookId));
        a.assertFailedWith(() -> service.getBookById(bookId), NotFoundException.class);

        a.assertFailedWith(() -> service.deleteBook(99L), NotFoundException.class);
    }
}