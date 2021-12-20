package ru.otus.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.dao.AuthorDao;

@ShellComponent
public class AuthorController {

    private final AuthorDao authorDao;

    public AuthorController(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @ShellMethod(value = "get author count command", key = {"ac", "getAuthorCount"})
    public void getAuthorCount() {
        authorDao.count();
    }
}
