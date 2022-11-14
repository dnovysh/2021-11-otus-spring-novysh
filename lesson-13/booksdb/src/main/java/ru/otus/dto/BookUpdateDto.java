package ru.otus.dto;

import java.time.LocalDate;

public record BookUpdateDto(
        String id,
        String title,
        Integer totalPages,
        String isbn,
        LocalDate publishedDate
) {
}
