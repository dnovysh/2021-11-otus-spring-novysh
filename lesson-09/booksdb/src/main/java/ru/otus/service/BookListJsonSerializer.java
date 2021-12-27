package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.core.entity.Book;

import java.util.List;

@Component
public class BookListJsonSerializer extends BaseJsonSerializer<List<Book>> {

    public BookListJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
