package um.feri.resource;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import um.feri.model.Book;
import um.feri.services.BookService;

import java.util.List;

@ApplicationScoped
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService bookService;

    @GET
    public Uni<Response> getBooks() {
        return bookService.getAllBooks().onItem().transform(list -> Response.status(200).entity(list).build());
    }

    @GET
    @Path("/{bookId}")
    public Uni<Response> getBookById(@PathParam("bookId") Long bookId) {
        return bookService.getBookById(bookId)
                .onItem().transform(book -> Response.status(200).entity(book).build());
    }

    @POST
    public Uni<Response> addBook(Book newBook) {
        return bookService.createBook(newBook).onItem().transform(book -> Response.status(201).entity(book).build());
    }

    @PUT
    @Path("/{bookId}")
    public Uni<Response> updateBook(@PathParam("bookId") Long bookId, Book updatedBook) {
        return bookService.updateBook(bookId, updatedBook).onItem().transform(x -> Response.status(204).build());
    }

    @DELETE
    @Path("/{bookId}")
    public Uni<Response> deleteBook(@PathParam("bookId") Long bookId) {
        return bookService.deleteBook(bookId).onItem().transform(x -> Response.status(204).build());
    }

}
