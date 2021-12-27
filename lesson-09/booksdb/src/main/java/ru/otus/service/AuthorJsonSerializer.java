package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.domain.Author;

@Component
public class AuthorJsonSerializer extends BaseJsonSerializer<Author> {

    public AuthorJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
