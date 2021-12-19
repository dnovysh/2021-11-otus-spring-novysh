package ru.otus.dao.helper;

import ru.otus.domain.Genre;

import java.util.List;
import java.util.Map;

public interface BookGenreDao {

    List<Genre> getAllByBookIdWithoutChildren(int bookId);

    Map<Integer, List<Genre>> getAllWithoutChildren();

    void insert(BookGenre bookGenre);

    boolean delete(BookGenre bookGenre);

    boolean exists(BookGenre bookGenre);
}
