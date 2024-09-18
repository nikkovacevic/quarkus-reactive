package um.feri.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import um.feri.model.Book;

import java.util.List;

@ApplicationScoped
public class BookService {

    public Uni<List<Book>> getAllBooks() {
        return Book.listAll(Sort.by("title"));
    }

    public Uni<Book> getBookById(Long id) {
        return Book.<Book>findById(id)
                .onItem().ifNull().failWith(new NotFoundException());
    }

    public Uni<Book> findBookIfExists(Book book) {
        return Book.find("isbn = ?1", book.getIsbn())
                .firstResult();
    }

    public Uni<Book> createBook(Book newBook) {
        return findBookIfExists(newBook)
                .onItem().ifNotNull().failWith(new WebApplicationException("Book already exists", Response.Status.CONFLICT))
                .onItem().ifNull().switchTo(() -> Panache.withTransaction(newBook::persist).replaceWith(newBook));
    }

    public Uni<Void> updateBook(Long id, Book updatedBook) {
        return getBookById(id)
                .onItem().transformToUni(existingBook -> {
                    existingBook.setTitle(updatedBook.getTitle());
                    existingBook.setIsbn(updatedBook.getIsbn());
                    existingBook.setPrice(updatedBook.getPrice());
                    existingBook.setYearOfRelease(updatedBook.getYearOfRelease());
                    existingBook.setAuthor(updatedBook.getAuthor());

                    return Panache.withTransaction(existingBook::persist)
                            .replaceWithVoid();
                });
    }

    public Uni<Void> deleteBook(Long id) {
        return Panache.withTransaction(() -> Book.deleteById(id))
                .onItem().transformToUni(deleted -> {
                    if (Boolean.FALSE.equals(deleted)) {
                        return Uni.createFrom().failure(new NotFoundException());
                    }
                    return Uni.createFrom().voidItem();
                });
    }
}
