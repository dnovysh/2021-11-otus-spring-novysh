package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;
}
