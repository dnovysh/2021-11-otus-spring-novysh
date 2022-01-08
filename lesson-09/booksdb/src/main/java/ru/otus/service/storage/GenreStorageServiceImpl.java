package ru.otus.service.storage;

import org.springframework.stereotype.Service;
import ru.otus.core.abstraction.GenreStorageService;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifierView;
import ru.otus.core.entity.GenreParentsView;
import ru.otus.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenreStorageServiceImpl implements GenreStorageService {

    private final GenreRepository genreRepository;

    public GenreStorageServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
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
    public Optional<GenreClassifierView> getGenreClassifierStartWithId(String id) {
        return genreRepository.getGenreClassifierStartWithId(id);
    }

    @Override
    public List<GenreClassifierView> getAllGenreClassifier() {
        return genreRepository.getAllGenreClassifier();
    }

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
