package ru.otus;

import lombok.SneakyThrows;
import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksdbApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(BooksdbApplication.class, args);

        Console.main(args);
    }
}
