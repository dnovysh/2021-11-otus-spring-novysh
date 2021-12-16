package ru.otus;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.dao.GenreDao;
import ru.otus.service.GenreSerializer;

@SpringBootApplication
public class BooksdbApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(BooksdbApplication.class, args);

        GenreDao genreDao = context.getBean(GenreDao.class);
        GenreSerializer genreSerializer = context.getBean(GenreSerializer.class);

        System.out.println(genreDao.count());

        System.out.println(genreSerializer.serialize(genreDao.getByIdWithoutChildren("57.20")));

        System.out.println(genreSerializer.serialize(genreDao.getEntireHierarchyStartWithRoot(), "    "));

        Console.main(args);
    }
}
