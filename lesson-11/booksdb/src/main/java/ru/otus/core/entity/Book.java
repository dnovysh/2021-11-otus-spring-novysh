package ru.otus.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.ISBN;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "total_pages")
    private Integer totalPages;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0")
    @Column(name = "rating")
    private BigDecimal rating;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ISBN
    @Column(name = "isbn")
    private String isbn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "published_date")
    private Date publishedDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_book_author"), updatable = false),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_author_book")))
    private Set<Author> authors;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_book_genre"), updatable = false),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_genre_book")))
    private Set<Genre> genres;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @JoinColumn(name = "book_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_book_review"),
            updatable = false, insertable = false)
    private List<Review> reviews;

    public Book(String title, Integer totalPages, BigDecimal rating,
                String isbn, Date publishedDate) {
        this.title = title;
        this.totalPages = totalPages;
        this.rating = rating;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id != null && id.equals(book.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}
