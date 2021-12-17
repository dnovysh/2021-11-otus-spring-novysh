package ru.otus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.otus.domain.Book;

import java.util.List;

public class BookJsonSerializer implements BookSerializer{

    private final ObjectMapper mapper;
    private final DefaultPrettyPrinter prettyPrinter;

    public BookJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        this.mapper = mapperBuilder.build();
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Override
    public String serialize(Book book) {
        try {
            return mapper.writeValueAsString(book);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(List<Book> books) {
        try {
            return mapper.writeValueAsString(books);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(List<Book> books, String indent) {
        DefaultPrettyPrinter.Indenter indenter =
                new DefaultIndenter(indent, DefaultIndenter.SYS_LF);
        prettyPrinter.indentObjectsWith(indenter);
        prettyPrinter.indentArraysWith(indenter);

        try {
            return mapper.writer(prettyPrinter).writeValueAsString(books);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
