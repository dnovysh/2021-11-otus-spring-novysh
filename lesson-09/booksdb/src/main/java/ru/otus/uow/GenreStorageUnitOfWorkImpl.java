package ru.otus.uow;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.abstraction.GenreStorageUnitOfWork;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifier;
import ru.otus.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreStorageUnitOfWorkImpl implements GenreStorageUnitOfWork {

    private final GenreRepository genreRepository;

    public GenreStorageUnitOfWorkImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return genreRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreClassifier> getGenreClassifierStartWithId(String id) {
        return genreRepository.getGenreClassifierStartWithId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GenreClassifier> getGenreClassifier() {
        return genreRepository.getGenreClassifier();
    }
}
