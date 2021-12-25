package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BooksdbApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(BooksdbApplication.class, args);
    }
}
