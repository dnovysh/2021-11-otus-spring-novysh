package ru.otus.core.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @Size(min = 1, max = 20)
    @Column(name = "id", nullable = false)
    protected String id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "parent_id")
    protected String parentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id != null && id.equals(genre.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}
