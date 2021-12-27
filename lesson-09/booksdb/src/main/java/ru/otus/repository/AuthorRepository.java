package ru.otus.repository;

import ru.otus.core.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    long count();

    Optional<Author> findById(Integer id);

    List<Author> findAll();
}
