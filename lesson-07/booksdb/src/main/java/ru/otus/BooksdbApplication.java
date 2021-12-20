package ru.otus;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.dao.BookDao;
import ru.otus.dao.GenreDao;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.service.BaseSerializer;
import ru.otus.service.BookJsonSerializer;
import ru.otus.service.GenreJsonSerializer;

@SpringBootApplication
public class BooksdbApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(BooksdbApplication.class, args);






//        GenreDao genreDao = context.getBean(GenreDao.class);
//        BaseSerializer<Genre> genreSerializer = context.getBean(GenreJsonSerializer.class);
//
//        System.out.println(genreDao.count());
//
//        System.out.println(genreSerializer.serialize(genreDao.getByIdWithoutPopulateChildrenList("57.20")));
//
//        System.out.println(genreSerializer.serialize(genreDao.getEntireHierarchyStartWithRoot(), "    "));
//
//        System.out.println("\n");
//
//        BookDao bookDao = context.getBean(BookDao.class);
//        BaseSerializer<Book> bookSerializer = context.getBean(BookJsonSerializer.class);
//
//        System.out.println(bookSerializer.serialize(bookDao.getAll(), "    "));

//        Console.main(args);
    }
}
