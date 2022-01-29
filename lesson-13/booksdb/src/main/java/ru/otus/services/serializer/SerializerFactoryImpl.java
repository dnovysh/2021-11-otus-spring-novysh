package ru.otus.services.serializer;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

@Service
public class SerializerFactoryImpl<T> implements SerializerFactory<T> {

    private final Jackson2ObjectMapperBuilder mapperBuilder;

    public SerializerFactoryImpl(Jackson2ObjectMapperBuilder mapperBuilder) {
        this.mapperBuilder = mapperBuilder;
    }

    @Override
    public BaseSerializer<T> getSerializer() {
        return new BaseJsonSerializer<T>(mapperBuilder);
    }
}
