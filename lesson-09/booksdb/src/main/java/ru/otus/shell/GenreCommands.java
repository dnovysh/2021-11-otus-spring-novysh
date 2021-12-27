package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.core.abstraction.BaseSerializer;
import ru.otus.core.abstraction.GenreStorageUnitOfWork;
import ru.otus.core.entity.Genre;

import java.util.List;
import java.util.Optional;

@ShellComponent
@ShellCommandGroup("Genre Commands")
public class GenreCommands {
    private final GenreStorageUnitOfWork genreStorage;
    private final BaseSerializer<Genre> genreSerializer;
    private final BaseSerializer<List<Genre>> genreListSerializer;

    public GenreCommands(GenreStorageUnitOfWork genreStorage,
                         BaseSerializer<Genre> genreSerializer,
                         BaseSerializer<List<Genre>> genreListSerializer) {
        this.genreStorage = genreStorage;
        this.genreSerializer = genreSerializer;
        this.genreListSerializer = genreListSerializer;
    }

    @ShellMethod(value = "get genre count command", key = {"g-count", "getGenreCount"})
    public void getGenreCount() {
        System.out.println(genreStorage.count());
    }

    @ShellMethod(value = "get genre by id without populate children list command",
            key = {"g-id", "getGenreById"})
    public void getGenreById(String id) {
        Optional<Genre> optionalGenre = genreStorage.findById(id);
        System.out.println(optionalGenre.isPresent()
                ? genreSerializer.serialize(optionalGenre.get())
                : "Genre not found"
        );
    }

    @ShellMethod(value = "get all genre without populate children list command",
            key = {"g-all", "getAllGenre"})
    public void getAllGenre(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Genre> genres = genreDao.getAllWithoutPopulateChildrenList();
        System.out.println(genreListSerializer.serialize(genres, indent));
    }

    @ShellMethod(value = "get genre hierarchy starting with id command",
            key = {"g-h-id", "getGenreHierarchyStartWithId"})
    public void getGenreHierarchyStartWithId(
            String id,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        Genre genre = genreDao.getEntireHierarchyStartWithId(id);
        System.out.println(genreSerializer.serialize(genre, indent));
    }

    @ShellMethod(value = "get genre hierarchy command",
            key = {"g-h-all", "getGenreHierarchy"})
    public void getGenreHierarchy(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Genre> genres = genreDao.getEntireHierarchyStartWithRoot();
        System.out.println(genreListSerializer.serialize(genres, indent));
    }
}
