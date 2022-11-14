package ru.otus.repositories;

import org.springframework.data.repository.Repository;
import ru.otus.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends Repository<Book, String>, BookRepositoryCustom {
    long count();

    Optional<Book> findById(String id);

    List<Book> findAll();

    Book save(Book book);
}
