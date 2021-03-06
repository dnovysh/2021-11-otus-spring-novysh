package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.core.abstraction.AuthorStorageService;
import ru.otus.core.abstraction.BaseSerializer;
import ru.otus.core.abstraction.SerializerFactory;
import ru.otus.core.entity.Author;

import java.util.List;

@ShellComponent
@ShellCommandGroup("Author Commands")
public class AuthorCommands {
    private final AuthorStorageService authorStorage;
    private final BaseSerializer<Author> authorSerializer;
    private final BaseSerializer<List<Author>> authorListSerializer;

    public AuthorCommands(AuthorStorageService authorStorage,
                          SerializerFactory<Author> authorSerializerFactory,
                          SerializerFactory<List<Author>> authorsSerializerFactory) {
        this.authorStorage = authorStorage;
        this.authorSerializer = authorSerializerFactory.getSerializer();
        this.authorListSerializer = authorsSerializerFactory.getSerializer();
    }

    @ShellMethod(value = "get author count command", key = {"a-count", "getAuthorCount"})
    public void getAuthorCount() {
        System.out.println(authorStorage.count());
    }

    @ShellMethod(value = "get author by id command", key = {"a-id", "getAuthorById"})
    public void getAuthorById(int id) {
        System.out.println(authorStorage.findById(id)
                .map(authorSerializer::serialize).orElse("Author not found")
        );
    }

    @ShellMethod(value = "get all author command", key = {"a-all", "getAllAuthor"})
    public void getAllAuthor(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Author> authors = authorStorage.findAll();
        System.out.println(authorListSerializer.serialize(authors, indent));
    }
}
