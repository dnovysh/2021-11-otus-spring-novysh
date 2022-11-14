package ru.otus.core.dto;

import ru.otus.core.entity.Book;

import java.math.BigDecimal;
import java.sql.Date;

public record BookUpdateDto(
        Integer id,
        String title,
        Integer totalPages,
        BigDecimal rating,
        String isbn,
        Date publishedDate
) {
}
