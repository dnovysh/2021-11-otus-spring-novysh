package ru.otus.core.abstraction;

import ru.otus.core.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorStorageService {

    long count();

    Optional<Author> findById(Integer id);

    List<Author> findAll();
}
