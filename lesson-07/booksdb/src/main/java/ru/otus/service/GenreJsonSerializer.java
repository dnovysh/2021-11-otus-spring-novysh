package ru.otus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.domain.Genre;

import java.util.List;

@Component
public class GenreJsonSerializer implements GenreSerializer {
    private final ObjectMapper mapper;
    private final DefaultPrettyPrinter prettyPrinter;

    public GenreJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        this.mapper = mapperBuilder.build();
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Override
    public String serialize(Genre genre) {
        try {
            return mapper.writeValueAsString(genre);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(List<Genre> genres) {
        try {
            return mapper.writeValueAsString(genres);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(List<Genre> genres, @NonNull String indent) {
        DefaultPrettyPrinter.Indenter indenter =
                new DefaultIndenter(indent, DefaultIndenter.SYS_LF);
        prettyPrinter.indentObjectsWith(indenter);
        prettyPrinter.indentArraysWith(indenter);

        try {
            return mapper.writer(prettyPrinter).writeValueAsString(genres);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
