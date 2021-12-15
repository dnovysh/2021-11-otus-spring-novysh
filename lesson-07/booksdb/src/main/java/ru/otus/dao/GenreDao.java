package ru.otus.dao;

import ru.otus.domain.Genre;

import java.util.List;

public interface GenreDao {

    int count();

    List<Genre> getAll();
}
