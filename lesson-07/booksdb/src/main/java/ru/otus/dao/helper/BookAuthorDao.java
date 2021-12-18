package ru.otus.dao.helper;

import ru.otus.domain.Author;

import java.util.List;
import java.util.Map;

public interface BookAuthorDao {

    List<Author> getAllByBookId(int bookId);

    Map<Integer, List<Author>> getAll();
}
