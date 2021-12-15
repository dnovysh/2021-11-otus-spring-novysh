package ru.otus.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public record Book(
        int id,
        String title,
        int totalPages,
        BigDecimal rating,
        String isbn,
        Date publishedDate,
        List<Author> authors,
        List<Genre> genres
        ) {
}
