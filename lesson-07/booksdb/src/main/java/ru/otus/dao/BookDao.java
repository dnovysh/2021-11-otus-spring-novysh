package ru.otus.dao;

import ru.otus.domain.Book;

import java.util.List;

public interface BookDao {

    int count();

    int insert(Book book);

    Book getById(int id);

    List<Book> getAll();

    boolean update(Book book);

    boolean deleteById(int id);
}
