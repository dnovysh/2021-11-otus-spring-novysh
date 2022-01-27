package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.core.projection.GenreClassifierProjection;

import java.util.List;

public interface GenreClassifierProjectionRepository
        extends JpaRepository<GenreClassifierProjection, String> {

    List<GenreClassifierProjection> findByParentIsNullOrderById();
}
