package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalPages;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String isbn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate publishedDate;

    private boolean deleted;

    private List<Author> authors;

    private List<Genre> genres;
}
