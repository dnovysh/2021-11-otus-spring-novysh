package ru.otus.dao;

import ru.otus.domain.Genre;

import java.util.List;

public interface GenreDao {

    int count();

    Genre getByIdWithoutChildren(String id);

    List<Genre> getAllWithoutChildren();

    Genre getEntireHierarchyStartWith(String id);

    List<Genre> getEntireHierarchyStartWithRoot();
}
