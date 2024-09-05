package um.feri.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import um.feri.model.Author;

import java.util.List;

@ApplicationScoped
public class AuthorService {

    public Uni<List<Author>> getAllAuthors() {
        return Author.listAll(Sort.by("surname"));
    }

    public Uni<Response> getAuthorById(Long id) {
        return Author.findById(id)
                .onItem().ifNotNull().transform(author -> Response.ok(author).build())
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    public Uni<Author> findIfAuthorExists(Author author) {
        return Author.find("name = ?1 and surname = ?2 and dateOfBirth = ?3",
                           author.getName(), author.getSurname(), author.getDateOfBirth())
                .firstResult();
    }

    public Uni<Author> addAuthor(Author author) {
        return findIfAuthorExists(author)
                .onItem().ifNotNull().transform(existingAuthor -> existingAuthor)
                .onItem().ifNull().switchTo(() -> Panache.withTransaction(author::persist).replaceWith(author));
    }

    public Uni<Response> updateAuthor(Long id, Author updatedAuthor) {
        return Author.<Author>findById(id)
                .onItem().ifNotNull().transformToUni(existingAuthor -> {
                    existingAuthor.setName(updatedAuthor.getName());
                    existingAuthor.setSurname(updatedAuthor.getSurname());
                    existingAuthor.setDateOfBirth(updatedAuthor.getDateOfBirth());

                    return Panache.withTransaction(existingAuthor::persist)
                            .replaceWith(Response.ok(existingAuthor).build());
                })
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    public Uni<Response> deleteAuthor(Long id) {
        return Panache.withTransaction(() -> Author.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(Response.Status.NO_CONTENT).build()
                        : Response.ok().status(Response.Status.NOT_FOUND).build());
    }

}
