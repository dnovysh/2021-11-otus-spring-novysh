package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.domain.Author;

import java.util.List;

@Component
public class AuthorListJsonSerializer extends BaseJsonSerializer<List<Author>> {

    public AuthorListJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
