package ru.otus.dao.helper;

import java.math.BigDecimal;
import java.sql.Date;

public record PlainBook(
        int id,
        String title,
        Integer totalPages,
        BigDecimal rating,
        String isbn,
        Date publishedDate
) {
}
