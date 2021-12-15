package ru.otus.dao;

import ru.otus.domain.Author;

import java.util.List;

public interface AuthorDao {

    int count();

    List<Author> getAll();
}
