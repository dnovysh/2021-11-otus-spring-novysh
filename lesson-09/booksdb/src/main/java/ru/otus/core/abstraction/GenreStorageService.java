package ru.otus.core.abstraction;

import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifierView;
import ru.otus.core.entity.GenreParentsView;

import java.util.List;
import java.util.Optional;

public interface GenreStorageService {

    long count();

    Optional<Genre> findById(String id);

    List<Genre> findAll();

    Optional<GenreClassifierView> getGenreClassifierStartWithId(String id);

    List<GenreClassifierView> getAllGenreClassifier();

    Optional<GenreParentsView> getGenrePathById(String id);
}
