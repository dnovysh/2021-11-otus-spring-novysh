package ru.otus.dao.helper;

import ru.otus.domain.Genre;

import java.util.List;

public interface BookGenreDao {
    List<Genre> getAllByBookId(int bookId);
}
