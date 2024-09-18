package um.feri.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import um.feri.model.Author;

import java.util.List;

@ApplicationScoped
public class AuthorService {

    public List<Author> getAllAuthors() {
        return Author.listAll(Sort.by("surname"));
    }

    public Author getAuthorById(Long id) {
        Author author = Author.findById(id);
        if (author == null) {
            throw new NotFoundException();
        }
        return author;
    }

    public Author findIfAuthorExists(Author author) {
        return Author.find("name = ?1 and surname = ?2 and dateOfBirth = ?3",
                           author.getName(), author.getSurname(), author.getDateOfBirth()).firstResult();
    }

    @Transactional
    public Author addAuthor(Author author) {
        Author existingAuthor = findIfAuthorExists(author);
        if (existingAuthor != null) {
            return existingAuthor;
        }
        author.persist();
        return author;
    }

    @Transactional
    public Response updateAuthor(Long id, Author author) {
        Author existingAuthor = getAuthorById(id);
        if (existingAuthor == null) {
            throw new NotFoundException();
        }
        existingAuthor.setName(author.getName());
        existingAuthor.setSurname(author.getSurname());
        existingAuthor.setDateOfBirth(author.getDateOfBirth());
        existingAuthor.persist();
        return Response.ok(existingAuthor).build();
    }

    @Transactional
    public Response deleteAuthor(Long id) {
        boolean deleted = Author.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        throw new NotFoundException();
    }
}
