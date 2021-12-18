package ru.otus;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import ru.otus.dao.GenreDao;
import ru.otus.domain.Genre;
import ru.otus.service.BaseJsonSerializer;
import ru.otus.service.BaseSerializer;
import ru.otus.service.GenreJsonSerializer;

@SpringBootApplication
public class BooksdbApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(BooksdbApplication.class, args);

        GenreDao genreDao = context.getBean(GenreDao.class);
        BaseSerializer<Genre> genreSerializer = context.getBean(GenreJsonSerializer.class);

        System.out.println(genreDao.count());

        System.out.println(genreSerializer.serialize(genreDao.getByIdWithoutChildren("57.20")));

        System.out.println(genreSerializer.serialize(genreDao.getEntireHierarchyStartWithRoot(), "    "));

        Console.main(args);
    }
}
