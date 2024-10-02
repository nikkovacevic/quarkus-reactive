package um.feri.spring.service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import um.feri.spring.model.Book;
import um.feri.spring.repository.BookRepository;
import um.feri.spring.service.BookService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService service;
    @Autowired
    private BookRepository bookRepository;

    private static Long bookId;

    @BeforeEach
    void setUp() {
        Book book = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        bookRepository.save(book);
        bookId = book.getId();
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
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

        assertThrows(ResponseStatusException.class, () -> service.getBookById(99L));
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
        assertEquals(2, bookRepository.count());
        assertNotNull(newBook.getId());

        Book existingBook = new Book("Murder on the Orient Express", "1234567890", 39.99, 1934, "Agatha Christie");
        assertThrows(ResponseStatusException.class, () -> service.createBook(existingBook));
    }

    @Test
    void updateBook() {
        Book book = new Book("Murder on the Orient Express", "1234567890", 29.99, 1934, "Agatha Christie");
        service.updateBook(bookId, book);
        Book updatedBook = service.getBookById(bookId);
        assertEquals(29.99, updatedBook.getPrice());

        assertThrows(ResponseStatusException.class, () -> service.updateBook(99L, book));
    }

    @Test
    void deleteBook() {
        service.deleteBook(bookId);
        assertThrows(ResponseStatusException.class, () -> service.getBookById(bookId));

        assertThrows(ResponseStatusException.class, () -> service.deleteBook(99L));
    }

}