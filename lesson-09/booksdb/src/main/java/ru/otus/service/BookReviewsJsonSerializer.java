package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.core.dto.BookReviewsViewDto;


@Component
public class BookReviewsJsonSerializer extends BaseJsonSerializer<BookReviewsViewDto> {

    public BookReviewsJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
