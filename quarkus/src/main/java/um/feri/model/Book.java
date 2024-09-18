package um.feri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends PanacheEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String isbn;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer yearOfRelease;
    @Column(nullable = false)
    private String author;
}
