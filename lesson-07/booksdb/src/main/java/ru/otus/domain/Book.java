package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public record Book(
        int id,
        String title,
        int totalPages,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal rating,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String isbn,
        Date publishedDate,
        List<Author> authors,
        List<Genre> genres
) {
}
