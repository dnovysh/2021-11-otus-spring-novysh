package ru.otus.dao.helper;

import ru.otus.domain.Author;

import java.util.List;

public interface BookAuthorDao {
    List<Author> getAllByBookIdWithoutChildren(int bookId);
}
