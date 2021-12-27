package ru.otus.uow;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.abstraction.AuthorStorageUnitOfWork;
import ru.otus.core.entity.Author;
import ru.otus.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorStorageUnitOfWorkImpl implements AuthorStorageUnitOfWork {

    private final AuthorRepository authorRepository;

    public AuthorStorageUnitOfWorkImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return authorRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> findById(Integer id) {
        return authorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
