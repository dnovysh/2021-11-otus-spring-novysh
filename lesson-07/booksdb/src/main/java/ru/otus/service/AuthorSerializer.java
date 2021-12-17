package ru.otus.service;

import ru.otus.domain.Author;

import java.util.List;

public interface AuthorSerializer {

    String serialize(Author author);

    String serialize(List<Author> authors);
}
