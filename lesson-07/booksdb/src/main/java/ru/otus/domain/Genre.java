package ru.otus.domain;

import java.util.List;

public record Genre(
    String id,
    String name,
    String parentId,
    List<Genre> genres
) {
}
