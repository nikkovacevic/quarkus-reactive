package um.feri.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import um.feri.model.Book;

import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    AuthorService authorService;

    public Uni<List<Book>> getAllBooks() {
        return Book.listAll(Sort.by("title"));
    }

    public Uni<Response> getBookById(Long id) {
        return Book.findById(id)
                .onItem().ifNotNull().transform(book -> Response.ok(book).build())
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    public Uni<Book> findIfBookExists(Book book) {
        return Book.find("isbn = ?1", book.getIsbn())
                .firstResult();
    }

    public Uni<RestResponse<Book>> createBook(Book book) {
        return findIfBookExists(book)
                .onItem().ifNotNull().transform(existingBook ->
                                                        RestResponse.status(Response.Status.CONFLICT, existingBook))
                .onItem().ifNull().switchTo(() ->
                                                    authorService.addAuthor(book.getAuthor())
                                                            .onItem().transformToUni(author -> {
                                                                book.setAuthor(author);
                                                                return Panache.withTransaction(book::persist)
                                                                        .replaceWith(RestResponse.status(Response.Status.CREATED, book));
                                                            }));
    }

    public Uni<Response> updateBook(Long id, Book updatedBook) {
        return Book.<Book>findById(id)
                .onItem().ifNotNull().transformToUni(existingBook -> {
                    existingBook.setTitle(updatedBook.getTitle());
                    existingBook.setIsbn(updatedBook.getIsbn());
                    existingBook.setPrice(updatedBook.getPrice());
                    existingBook.setYearOfRelease(updatedBook.getYearOfRelease());

                    return Panache.withTransaction(existingBook::persist)
                            .replaceWith(Response.ok(existingBook).build());
                })
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    public Uni<Response> deleteBook(Long id) {
        return Panache.withTransaction(() -> Book.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(Response.Status.NO_CONTENT).build()
                        : Response.ok().status(Response.Status.NOT_FOUND).build());
    }
}
