package um.feri.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import um.feri.model.Author;
import um.feri.model.Book;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceTest {

    @Inject
    BookService service;

    @Test
    @Order(1)
    void createBook() {
        Author author = new Author("Agatha", "Christie", LocalDate.of(1809, 9, 15));
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, author);

        Book newBook = service.createBook(book);
        assertNotNull(newBook);
        assertEquals("Murder on the Orient Express", newBook.getTitle());
        assertEquals("1234567890", newBook.getIsbn());
        assertEquals(39.99, newBook.getPrice());

        assertEquals(1, Book.count());
        assertEquals(1, Author.count());

        Book existingBook = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, author);
        assertThrows(WebApplicationException.class, () -> service.createBook(existingBook));
        assertEquals(1, Book.count());

        Author newAuthor = new Author("J. R. R.", "Tolkien", LocalDate.of(1892, 1, 3));
        Book bookWithNewAuthor = new Book("Lord of the Rings", "123456789", 39.99, 1954, newAuthor);
        Book newBook2 = service.createBook(bookWithNewAuthor);
            assertNotNull(newBook2);
            assertEquals("Lord of the Rings", newBook2.getTitle());
            assertEquals("123456789", newBook2.getIsbn());
            assertEquals(39.99, newBook2.getPrice());


        assertEquals(2, Book.count());
        assertEquals(2, Author.count());
    }

    @Test
    @Order(2)
    void getAllBooks() {
        List<Book> books = service.getAllBooks();
        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    @Order(3)
    void getBookById() {
        Book book = service.getBookById(1L);
        assertNotNull(book);
        assertEquals("Murder on the Orient Express", book.getTitle());
        assertEquals("Christie", book.getAuthor().getSurname());

        assertThrows(WebApplicationException.class, () -> service.getBookById(99L));
    }

    @Test
    @Order(4)
    void findIfBookExists() {
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, null);
        Book existingBook = service.findIfBookExists(book);
        assertNotNull(existingBook);
        assertEquals("Murder on the Orient Express", existingBook.getTitle());
        assertEquals(1934, existingBook.getYearOfRelease());
    }

    @Test
    @Order(5)
    void updateBook() {
        Book book = new Book("Murder on the Orient Express", "1234567890", 29.99, 1934, null);
        service.updateBook(1L, book);
        Book updatedBook = service.getBookById(1L);
        assertNotNull(updatedBook);
        assertEquals(29.99, updatedBook.getPrice());

        assertThrows(NotFoundException.class, () -> service.updateBook(99L, book));
    }

    @Test
    @Order(6)
    void deleteBook() {
        service.deleteBook(1L);
        assertThrows(NotFoundException.class, () -> service.getBookById(1L));
        assertThrows(NotFoundException.class, () -> service.deleteBook(99L));
    }
}