package ru.otus.repository;

import ru.otus.core.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    public long count();

    Optional<Book> findById(Integer id);

    public List<Book> findAll();

    Book save(Book review);

    public void deleteById(Integer id);
}
