package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.core.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, String> {
}
