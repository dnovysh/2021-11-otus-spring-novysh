package ru.otus.repositories;

import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;

public interface BookRepositoryCustom {
    void deleteById(String id);

    void restoreById(String id);

    void addAuthor(String bookId, Author author);

    void removeAuthor(String bookId, Author author);

//
//    Book addGenre(String bookId, Genre genre);
//
//    Book removeGenre(String bookId, Genre genre);
}
