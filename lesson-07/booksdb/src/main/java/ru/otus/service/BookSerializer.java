package ru.otus.service;

import ru.otus.domain.Book;

import java.util.List;

public interface BookSerializer {

    String serialize(Book book);

    String serialize(List<Book> books);

    String serialize(List<Book> books, String indent);
}
