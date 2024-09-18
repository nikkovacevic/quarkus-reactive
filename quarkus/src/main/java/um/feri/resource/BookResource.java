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
import um.feri.service.BookService;

import java.util.List;

@ApplicationScoped
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService bookService;

    @GET
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{bookId}")
    public Response getBookById(@PathParam("bookId") Long bookId) {
        Book bookById = bookService.getBookById(bookId);
        return Response.status(200).entity(bookById).build();
    }

    @POST
    public Response addBook(Book book) {
        bookService.createBook(book);
        return Response.status(201).build();
    }

    @PUT
    @Path("/{bookId}")
    public Response updateBook(@PathParam("bookId") Long bookId, Book updatedBook) {
        bookService.updateBook(bookId, updatedBook);
        return Response.status(204).build();
    }

    @DELETE
    @Path("/{bookId}")
    public Response deleteBook(@PathParam("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return Response.status(204).build();
    }
}
