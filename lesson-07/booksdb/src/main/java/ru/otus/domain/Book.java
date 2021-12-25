package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public record Book(
        int id,
        String title,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer totalPages,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal rating,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String isbn,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Date publishedDate,
        List<Author> authors,
        List<Genre> genres
) {
}
