package ru.otus.dao.helper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Dao для работы с пёрсонами должно")
@JdbcTest
@Import(BookAuthorDaoImpl.class)
class BookAuthorDaoImplTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllByBookId() {
    }

    @Test
    void getAll() {
    }

    @Test
    void insert() {
    }

    @Test
    void delete() {
    }

    @Test
    @Rollback
    void exists() {
    }
}