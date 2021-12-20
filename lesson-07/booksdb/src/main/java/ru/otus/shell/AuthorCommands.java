package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dao.AuthorDao;
import ru.otus.domain.Author;
import ru.otus.service.BaseSerializer;

import java.util.List;

@ShellComponent
@ShellCommandGroup("Author Commands")
public class AuthorCommands {
    private final AuthorDao authorDao;
    private final BaseSerializer<Author> authorSerializer;
    private final BaseSerializer<List<Author>> authorListSerializer;

    public AuthorCommands(AuthorDao authorDao,
                          BaseSerializer<Author> authorSerializer,
                          BaseSerializer<List<Author>> authorListSerializer) {
        this.authorDao = authorDao;
        this.authorSerializer = authorSerializer;
        this.authorListSerializer = authorListSerializer;
    }

    @ShellMethod(value = "get author count command", key = {"a-count", "getAuthorCount"})
    public void getAuthorCount() {
        System.out.println(authorDao.count());
    }

    @ShellMethod(value = "get author by id command", key = {"a-id", "getAuthorById"})
    public void getAuthorById(int id) {
        System.out.println(authorSerializer.serialize(authorDao.getById(id)));
    }

    @ShellMethod(value = "get all author command", key = {"a-all", "getAllAuthor"})
    public void getAllAuthor(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Author> authors = authorDao.getAll();
        System.out.println(authorListSerializer.serialize(authors, indent));
    }
}
