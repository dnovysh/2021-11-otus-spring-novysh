package ru.otus.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 50)
    @Column(name = "middle_name")
    private String middleName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 100)
    @Column(name = "last_name")
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id != null && id.equals(author.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}
