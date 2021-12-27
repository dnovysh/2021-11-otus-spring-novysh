package ru.otus.dao.dto;

import java.math.BigDecimal;
import java.sql.Date;

public record BookUpdateDto(
        int id,
        String title,
        Integer totalPages,
        BigDecimal rating,
        String isbn,
        Date publishedDate
) {
}
