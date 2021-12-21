package ru.otus.dao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.sql.Date;

public record BookInsertDto(
        String title,
        Integer totalPages,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal rating,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String isbn,
        Date publishedDate
) {
}
