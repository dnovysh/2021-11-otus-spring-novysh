package ru.otus.core.abstraction;

import ru.otus.core.entity.Genre;
import ru.otus.core.projection.GenreClassifierProjection;
import ru.otus.core.projection.GenreParentsProjection;

import java.util.List;
import java.util.Optional;

public interface GenreStorageService {

    long count();

    Optional<Genre> findById(String id);

    List<Genre> findAll();

    Optional<GenreClassifierProjection> getGenreClassifierStartWithId(String id);

    List<GenreClassifierProjection> getAllGenreClassifier();

    Optional<GenreParentsProjection> getGenrePathById(String id);
}
