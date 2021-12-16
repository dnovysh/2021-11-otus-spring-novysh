package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record Genre(
    String id,
    String name,
    String parentId,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Genre> genres
) {
}
