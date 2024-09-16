package um.feri.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import um.feri.model.Author;

import java.util.List;

@ApplicationScoped
public class AuthorService {

    public List<Author> getAllAuthors() {
        return Author.listAll(Sort.by("surname"));
    }

    public Author getAuthorById(Long id) {
        return Author.findById(id);
    }

    private Author findIfAuthorExists(Author author) {
        return Author.find("name = ?1 and surname = ?2 and dateOfBirth = ?3",
                           author.getName(), author.getSurname(), author.getDateOfBirth()).firstResult();
    }

    public Author addAuthor(Author author) {
        Author existingAuthor = findIfAuthorExists(author);
        if (existingAuthor != null) {
            return existingAuthor;
        }
        author.persist();
        return author;
    }

    public void updateAuthor(Long id, Author author) {
        Author existingAuthor = getAuthorById(id);
        if (existingAuthor == null) {
            //throw 404
            throw new NotFoundException();
        }
        existingAuthor.setName(author.getName());
        existingAuthor.setSurname(author.getSurname());
        existingAuthor.setDateOfBirth(author.getDateOfBirth());
        existingAuthor.persist();
    }

    public void deleteAuthor(Long id) {
        Author.deleteById(id);
    }
}
