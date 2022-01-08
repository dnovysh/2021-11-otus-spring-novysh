package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.core.abstraction.BaseSerializer;
import ru.otus.core.abstraction.GenreStorageService;
import ru.otus.core.abstraction.SerializerFactory;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifierView;
import ru.otus.core.entity.GenreParentsView;

import java.util.List;
import java.util.Optional;

@ShellComponent
@ShellCommandGroup("Genre Commands")
public class GenreCommands {
    private final GenreStorageService genreStorage;
    private final BaseSerializer<Genre> genreSerializer;
    private final BaseSerializer<List<Genre>> genreListSerializer;
    private final BaseSerializer<GenreClassifierView> genreClassifierSerializer;
    private final BaseSerializer<List<GenreClassifierView>> genreClassifierListSerializer;
    private final BaseSerializer<GenreParentsView> genreParentsViewSerializer;

    public GenreCommands(GenreStorageService genreStorage,
                         SerializerFactory<Genre> genreSerializerFactory,
                         SerializerFactory<List<Genre>> genreListSerializerFactory,
                         SerializerFactory<GenreClassifierView> genreClassifierSerializerFactory,
                         SerializerFactory<List<GenreClassifierView>> genreClassifierListSerializerFactory,
                         SerializerFactory<GenreParentsView> genreParentsViewSerializerFactory) {
        this.genreStorage = genreStorage;
        this.genreSerializer = genreSerializerFactory.getSerializer();
        this.genreListSerializer = genreListSerializerFactory.getSerializer();
        this.genreClassifierSerializer = genreClassifierSerializerFactory.getSerializer();
        this.genreClassifierListSerializer = genreClassifierListSerializerFactory.getSerializer();
        this.genreParentsViewSerializer = genreParentsViewSerializerFactory.getSerializer();
    }

    @ShellMethod(value = "get genre count command", key = {"g-count", "getGenreCount"})
    public void getGenreCount() {
        System.out.println(genreStorage.count());
    }

    @ShellMethod(value = "get genre by id command",
            key = {"g-id", "getGenreById"})
    public void getGenreById(String id) {
        System.out.println(genreStorage.findById(id)
                .map(genreSerializer::serialize).orElse("Genre not found")
        );
    }

    @ShellMethod(value = "get all genre command",
            key = {"g-all", "getAllGenre"})
    public void getAllGenre(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Genre> genres = genreStorage.findAll();
        System.out.println(genreListSerializer.serialize(genres, indent));
    }

    @ShellMethod(value = "get genre hierarchy starting with id command",
            key = {"g-h-id", "getGenreHierarchyStartWithId"})
    public void getGenreHierarchyStartWithId(
            String id,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        Optional<GenreClassifierView> optionalGenreClassifier = genreStorage
                .getGenreClassifierStartWithId(id);
        System.out.println(optionalGenreClassifier
                .map(g -> genreClassifierSerializer.serialize(g, indent))
                .orElse("Genre not found")
        );
    }

    @ShellMethod(value = "get genre hierarchy command",
            key = {"g-h-all", "getGenreHierarchy"})
    public void getGenreHierarchy(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<GenreClassifierView> genreClassifiers = genreStorage.getAllGenreClassifier();
        System.out.println(genreClassifierListSerializer.serialize(genreClassifiers, indent));
    }

    @ShellMethod(value = "get genre parents by node id command",
            key = {"g-p-id", "getGenreParentsById"})
    public void getGenreParentsById(
            String id,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        Optional<GenreParentsView> optionalGenreParents = genreStorage
                .getGenrePathById(id);
        System.out.println(optionalGenreParents
                .map(g -> genreParentsViewSerializer.serialize(g, indent))
                .orElse("Genre not found")
        );
    }
}
