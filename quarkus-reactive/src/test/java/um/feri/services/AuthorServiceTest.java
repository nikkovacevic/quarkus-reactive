package um.feri.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.quarkus.vertx.VertxContextSupport;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import um.feri.TransactionalUniAsserterInterceptor;
import um.feri.model.Author;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@RunOnVertxContext
class AuthorServiceTest {

    @Inject
    AuthorService service;

    @Test
    @RunOnVertxContext
    public void testGetAllAuthors(TransactionalUniAsserter a) {
        a.execute(() -> new Author("J.K.", "Rowling", LocalDate.of(1965, 7, 31)).persist());

        a.assertEquals(Author::count, 3L);

        a.execute(Author::deleteAll);
    }
}