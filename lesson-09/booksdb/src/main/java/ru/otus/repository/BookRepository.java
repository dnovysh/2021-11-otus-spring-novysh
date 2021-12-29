package ru.otus.repository;

import ru.otus.core.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    long count();

    Optional<Book> findById(Integer id);

    List<Book> findAll();

    Book save(Book review);

    void deleteById(Integer id);
}
