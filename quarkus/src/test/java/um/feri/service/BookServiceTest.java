package um.feri.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import um.feri.model.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class BookServiceTest {

    private static Long bookId;
    @Inject
    BookService service;

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
    void getAllBooks() {
        List<Book> books = service.getAllBooks();
        assertEquals(1, books.size());
    }

    @Test
    void getBookById() {
        Book book = service.getBookById(bookId);
        assertEquals("Murder on the Orient Express", book.getTitle());
        assertEquals("1234567890", book.getIsbn());
        assertEquals(39.99, book.getPrice());
        assertEquals(1934, book.getYearOfRelease());
        assertEquals("Agatha Christie", book.getAuthor());

        assertThrows(WebApplicationException.class, () -> service.getBookById(99L));
    }

    @Test
    void findIfBookExists() {
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        Book existingBook = service.findBookIfExists(book);

        assertEquals("Murder on the Orient Express", existingBook.getTitle());
        assertEquals("1234567890", existingBook.getIsbn());
        assertEquals(39.99, existingBook.getPrice());
        assertEquals(1934, existingBook.getYearOfRelease());
        assertEquals("Agatha Christie", existingBook.getAuthor());
    }

    @Test
    void createBook() {
        Book newBook = new Book("Lord of the Rings", "123456789", 39.99, 1954, "J. R. R. Tolkien");
        newBook = service.createBook(newBook);
        assertEquals(2, Book.count());
        assertNotNull(newBook.id);

        Book existingBook = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        assertThrows(WebApplicationException.class, () -> service.createBook(existingBook));
    }

    @Test
    void updateBook() {
        Book book = new Book("Murder on the Orient Express", "1234567890", 29.99, 1934, "Agatha Christie");
        service.updateBook(bookId, book);
        Book updatedBook = service.getBookById(bookId);
        assertEquals(29.99, updatedBook.getPrice());

        assertThrows(NotFoundException.class, () -> service.updateBook(99L, book));
    }

    @Test
    void deleteBook() {
        service.deleteBook(bookId);
        assertThrows(NotFoundException.class, () -> service.getBookById(bookId));
        assertThrows(NotFoundException.class, () -> service.deleteBook(99L));
    }
}