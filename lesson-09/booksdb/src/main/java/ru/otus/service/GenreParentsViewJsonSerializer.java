package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.core.entity.GenreParentsView;

@Component
public class GenreParentsViewJsonSerializer extends BaseJsonSerializer<GenreParentsView> {

    public GenreParentsViewJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
