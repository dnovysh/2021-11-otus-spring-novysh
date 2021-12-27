package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.core.entity.GenreClassifierView;

import java.util.List;

@Component
public class GenreClassifierListJsonSerializer extends BaseJsonSerializer<List<GenreClassifierView>> {

    public GenreClassifierListJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
