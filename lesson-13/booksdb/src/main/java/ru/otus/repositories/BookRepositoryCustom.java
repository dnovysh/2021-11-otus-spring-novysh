package ru.otus.repositories;

import ru.otus.model.Author;
import ru.otus.model.Genre;

public interface BookRepositoryCustom {
    void deleteById(String id);

    void restoreById(String id);

    void addAuthor(String bookId, Author author);

    void removeAuthor(String bookId, Author author);

    void addGenre(String bookId, Genre genre);

    void removeGenre(String bookId, Genre genre);
}
