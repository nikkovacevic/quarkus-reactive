package um.feri.spring.service;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import um.feri.spring.model.Book;
import um.feri.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getAllBooks() {
        return repository.findAll(Sort.by("title"));
    }

    public Book getBookById(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return book.get();
    }

    public Book findBookIfExists(Book book) {
        return repository.findBookByIsbn(book.getIsbn()).orElse(null);
    }

    public Book createBook(Book newBook) {
        Book book = findBookIfExists(newBook);
        if (book != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        repository.save(newBook);
        return newBook;
    }

    public void updateBook(Long id, Book newBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(newBook.getTitle());
        existingBook.setIsbn(newBook.getIsbn());
        existingBook.setPrice(newBook.getPrice());
        existingBook.setYearOfRelease(newBook.getYearOfRelease());
        existingBook.setAuthor(newBook.getAuthor());
        repository.save(existingBook);
    }

    public void deleteBook(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

}
