package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.core.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
