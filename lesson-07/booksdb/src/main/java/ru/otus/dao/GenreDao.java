package ru.otus.dao;

import ru.otus.domain.Genre;

import java.util.List;

public interface GenreDao {

    int count();

    Genre getByIdWithoutPopulateChildrenList(String id);

    List<Genre> getAllWithoutPopulateChildrenList();

    Genre getEntireHierarchyStartWithId(String id);

    List<Genre> getEntireHierarchyStartWithRoot();
}
