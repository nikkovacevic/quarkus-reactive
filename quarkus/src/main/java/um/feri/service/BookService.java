package um.feri.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import um.feri.model.Author;
import um.feri.model.Book;

import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    AuthorService authorService;

    public List<Book> getAllBooks() {
        return Book.listAll(Sort.by("title"));
    }

    public Book getBookById(Long id) {
        Book book = Book.findById(id);
        if (book == null) {
            throw new NotFoundException();
        }
        return book;
    }

    public Book findIfBookExists(Book book) {
        return Book.find("isbn = ?1", book.getIsbn())
                .firstResult();
    }

    @Transactional
    public void createBook(Book newBook) {
        Book book = findIfBookExists(newBook);
        if (book != null) {
            throw new WebApplicationException("Book already exists", Response.Status.CONFLICT);
        }
        Author author = authorService.addAuthor(newBook.getAuthor());
        newBook.setAuthor(author);
        newBook.persist();
    }

    @Transactional
    public void updateBook(Long id, Book newBook) {
        Book existingBook = getBookById(id);
        if (existingBook == null) {
            throw new NotFoundException();
        }
        existingBook.setTitle(newBook.getTitle());
        existingBook.setIsbn(newBook.getIsbn());
        existingBook.setPrice(newBook.getPrice());
        existingBook.setYearOfRelease(newBook.getYearOfRelease());
        existingBook.persist();
    }

    @Transactional
    public void deleteBook(Long id) {
        boolean deleted = Book.deleteById(id);
        if (!deleted) {
            throw new NotFoundException();
        }
    }
}
