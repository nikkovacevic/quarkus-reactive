package um.feri.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "yearofrelease", nullable = false)
    private Integer yearOfRelease;
    @Column(name = "author", nullable = false)
    private String author;

    public Book(String title, String isbn, Double price, Integer yearOfRelease, String author) {
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.yearOfRelease = yearOfRelease;
        this.author = author;
    }
}
