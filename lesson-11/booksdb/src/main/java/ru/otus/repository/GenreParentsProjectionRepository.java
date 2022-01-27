package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.core.projection.GenreParentsProjection;

public interface GenreParentsProjectionRepository
        extends JpaRepository<GenreParentsProjection, String> {
}
