package um.feri.services;

import org.junit.jupiter.api.Test;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import jakarta.inject.Inject;
import um.feri.model.Author;

import java.time.LocalDate;

@QuarkusTest
@RunOnVertxContext
class AuthorServiceTest {

    @Inject
    AuthorService service;

    @Test
    @RunOnVertxContext
    public void testGetAllAuthors(TransactionalUniAsserter a) {
        a.execute( () -> new Author( "J.K.", "Rowling", LocalDate.of( 1965, 7, 31 ) ).persist() );

        // Panache doesn't support  method reference
        a.assertEquals( () -> Author.count(), 8L );
        a.execute( () -> Author.deleteAll() );
    }

}