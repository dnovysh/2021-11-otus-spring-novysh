package ru.otus.repository;

import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifier;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    long count();

    Optional<Genre> findById(String id);

    List<Genre> findAll();

    Optional<GenreClassifier> getGenreClassifierStartWithId(String id);

    List<GenreClassifier> getGenreClassifier();
}
