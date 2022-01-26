package ru.otus.service.serializer;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import ru.otus.core.abstraction.BaseSerializer;
import ru.otus.core.abstraction.SerializerFactory;

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
