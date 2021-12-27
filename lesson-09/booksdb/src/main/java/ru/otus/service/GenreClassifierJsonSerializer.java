package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.core.entity.GenreClassifierView;

@Component
public class GenreClassifierJsonSerializer extends BaseJsonSerializer<GenreClassifierView> {

    public GenreClassifierJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
