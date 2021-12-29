package ru.otus.service;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import ru.otus.core.entity.Review;

import java.util.List;

@Component
public class ReviewListJsonSerializer extends BaseJsonSerializer<List<Review>> {

    public ReviewListJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        super(mapperBuilder);
    }
}
