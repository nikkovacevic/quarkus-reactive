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
import um.feri.model.Author;
import um.feri.services.AuthorService;

import java.util.List;

@ApplicationScoped
@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @Inject
    AuthorService authorService;

    @GET
    public Uni<List<Author>> getAuthors() {
        return authorService.getAllAuthors();
    }

    @GET
    @Path("/{authorId}")
    public Uni<Response> getAuthorById(@PathParam("authorId") Long authorId) {
        return authorService.getAuthorById(authorId);
    }

    @POST
    public Uni<Author> create(Author author) {
        return authorService.addAuthor(author);
    }

    @PUT
    @Path("/{authorId}")
    public Uni<Response> updateAuthor(@PathParam("authorId") Long authorId, Author updatedAuthor) {
        return authorService.updateAuthor(authorId, updatedAuthor);
    }

    @DELETE
    @Path("/{authorId}")
    public Uni<Response> deleteAuthor(@PathParam("authorId") Long authorId) {
        return authorService.deleteAuthor(authorId);
    }
}
