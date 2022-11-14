package ru.otus.core.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "genre")
public class GenreParentsProjection implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    protected String id;

    @Column(name = "name", nullable = false)
    protected String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private GenreParentsProjection parent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private GenreParentsProjection child;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreParentsProjection genreClassifier = (GenreParentsProjection) o;
        return id != null && id.equals(genreClassifier.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}
