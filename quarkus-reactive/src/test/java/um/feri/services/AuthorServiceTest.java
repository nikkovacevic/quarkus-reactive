package um.feri.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
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
    @Order( 1 )
    public void testGetAllAuthors(TransactionalUniAsserter a) {
        a.execute( () -> new Author( "J.K.", "Rowling", LocalDate.of( 1965, 7, 31 ) ).persist() );
        a.assertEquals( () -> Author.count(), 6L );
    }

    @Test
    @RunOnVertxContext
    @Order( 2 )
    public void testService(TransactionalUniAsserter a) {
        a.assertThat( () -> service.getAllAuthors(), authors -> {
            Assertions.assertNotNull(authors );
            // This should fail since there are 6 inserted authors, but it passes
            Assertions.assertEquals( 7, authors.size() );
        } );
    }

}