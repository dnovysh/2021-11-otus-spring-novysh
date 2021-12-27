package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.core.entity.Genre;

import java.util.List;

@Component
public class GenreListJsonSerializer extends BaseJsonSerializer<List<Genre>> {

    public GenreListJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
