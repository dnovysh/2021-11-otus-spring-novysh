package ru.otus.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GenreClassifier extends Genre {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "parent_id")
    private List<GenreClassifier> children;
}
