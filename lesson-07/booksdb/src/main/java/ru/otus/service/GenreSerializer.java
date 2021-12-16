package ru.otus.service;

import ru.otus.domain.Genre;

import java.util.List;

public interface GenreSerializer {

    String serialize(Genre genre);

    String serialize(List<Genre> genres);

    String serialize(List<Genre> genres, String indent);
}
