package ru.otus.services.storage;

import org.springframework.stereotype.Service;
import ru.otus.dto.BookUpdateDto;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookStorageImpl implements BookStorage {

    private final BookRepository bookRepository;

    public BookStorageImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public long count() {
        return bookRepository.count();
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book update(BookUpdateDto bookUpdateDto) {
        var optionalBook = this.findById(bookUpdateDto.id());
        if (optionalBook.isEmpty()) {
            return null;
        }
        var book = optionalBook.get();
        if (book.isDeleted()) {
            return null;
        }
        var changedBook = Book.builder()
                .id(book.getId())
                .title(bookUpdateDto.title())
                .totalPages(bookUpdateDto.totalPages())
                .isbn(bookUpdateDto.isbn())
                .publishedDate(bookUpdateDto.publishedDate())
                .authors(book.getAuthors())
                .genres(book.getGenres())
                .build();
        return bookRepository.save(changedBook);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void restoreById(String id) {
        bookRepository.restoreById(id);
    }

    @Override
    public void addAuthor(String bookId, Author author) {
        bookRepository.addAuthor(bookId, author);
    }

    @Override
    public void removeAuthor(String bookId, Author author) {
        bookRepository.removeAuthor(bookId, author);
    }

    @Override
    public void addGenre(String bookId, Genre genre) {
        bookRepository.addGenre(bookId, genre);
    }

    @Override
    public void removeGenre(String bookId, Genre genre) {
        bookRepository.removeGenre(bookId, genre);
    }
}
