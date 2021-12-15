package ru.otus.dao;

import ru.otus.domain.Book;

import java.util.List;

public interface BookDao {

    int count();

    void insert(Book person);

    Book getById(long id);

    List<Book> getAll();

    void deleteById(long id);
}
