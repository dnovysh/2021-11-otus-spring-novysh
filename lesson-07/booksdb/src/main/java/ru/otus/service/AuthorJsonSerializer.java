package ru.otus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.otus.domain.Author;

import java.util.List;

public class AuthorJsonSerializer implements AuthorSerializer {

    private final ObjectMapper mapper;

    public AuthorJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        this.mapper = mapperBuilder.build();
    }

    @Override
    public String serialize(Author author) {
        try {
            return mapper.writeValueAsString(author);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(List<Author> authors) {
        try {
            return mapper.writeValueAsString(authors);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
