package ru.otus.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@NamedEntityGraph(name = "Review-bookId",
        attributeNodes = @NamedAttributeNode("bookId"))
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne()
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "book_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_book_review"),
            nullable = false)
    private BookId bookId;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "title", nullable = false)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 2000)
    @Column(name = "text")
    private String text;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0")
    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "review_date", nullable = false)
    private Date reviewDate;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public Review(BookId bookId, String title, String text, BigDecimal rating) {
        this.bookId = bookId;
        this.title = title;
        this.text = text;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id != null && id.equals(review.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}
