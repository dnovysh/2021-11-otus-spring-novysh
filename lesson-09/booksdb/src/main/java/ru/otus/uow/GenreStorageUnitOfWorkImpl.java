package ru.otus.uow;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.abstraction.GenreStorageUnitOfWork;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifierView;
import ru.otus.core.entity.GenreParentsView;
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
    public Optional<GenreClassifierView> getGenreClassifierStartWithId(String id) {
        return genreRepository.getGenreClassifierStartWithId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GenreClassifierView> getAllGenreClassifier() {
        return genreRepository.getAllGenreClassifier();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreParentsView> getGenrePathById(String id) {
        var genreNode = genreRepository.getGenreParentsById(id);
        if (genreNode.isEmpty()) {
            return genreNode;
        }
        GenreParentsView genreParentsView = genreNode.get();
        while (true) {
            if (genreParentsView.getParent() == null) {
                return Optional.of(genreParentsView);
            }
            genreParentsView.getParent().setChild(genreParentsView);
            genreParentsView = genreParentsView.getParent();
        }
    }
}
