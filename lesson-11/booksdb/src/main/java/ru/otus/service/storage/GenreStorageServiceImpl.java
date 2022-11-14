package ru.otus.service.storage;

import org.springframework.stereotype.Service;
import ru.otus.core.abstraction.GenreStorageService;
import ru.otus.core.entity.Genre;
import ru.otus.core.projection.GenreClassifierProjection;
import ru.otus.core.projection.GenreParentsProjection;
import ru.otus.repository.GenreClassifierProjectionRepository;
import ru.otus.repository.GenreParentsProjectionRepository;
import ru.otus.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenreStorageServiceImpl implements GenreStorageService {

    private final GenreRepository genreRepository;
    private final GenreClassifierProjectionRepository genreClassifierRepository;
    private final GenreParentsProjectionRepository genreParentsRepository;

    public GenreStorageServiceImpl(GenreRepository genreRepository,
                                   GenreClassifierProjectionRepository genreClassifierRepository,
                                   GenreParentsProjectionRepository genreParentsRepository) {
        this.genreRepository = genreRepository;
        this.genreClassifierRepository = genreClassifierRepository;
        this.genreParentsRepository = genreParentsRepository;
    }

    @Override
    public long count() {
        return genreRepository.count();
    }

    @Override
    public Optional<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<GenreClassifierProjection> getGenreClassifierStartWithId(String id) {
        return genreClassifierRepository.findById(id);
    }

    @Override
    public List<GenreClassifierProjection> getAllGenreClassifier() {
        return genreClassifierRepository.findByParentIsNullOrderById();
    }

    @Override
    public Optional<GenreParentsProjection> getGenrePathById(String id) {
        var genreNode = genreParentsRepository.findById(id);
        if (genreNode.isEmpty()) {
            return genreNode;
        }
        GenreParentsProjection genreParentsView = genreNode.get();
        while (true) {
            if (genreParentsView.getParent() == null) {
                return Optional.of(genreParentsView);
            }
            genreParentsView.getParent().setChild(genreParentsView);
            genreParentsView = genreParentsView.getParent();
        }
    }
}
