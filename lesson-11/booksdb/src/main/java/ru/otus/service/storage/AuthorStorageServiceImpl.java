package ru.otus.service.storage;

import org.springframework.stereotype.Service;
import ru.otus.core.abstraction.AuthorStorageService;
import ru.otus.core.entity.Author;
import ru.otus.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorStorageServiceImpl implements AuthorStorageService {

    private final AuthorRepository authorRepository;

    public AuthorStorageServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public long count() {
        return authorRepository.count();
    }

    @Override
    public Optional<Author> findById(Integer id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
