package ru.otus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public abstract class BaseJsonSerializer<T> implements BaseSerializer<T> {

    private final ObjectMapper mapper;
    private final DefaultPrettyPrinter prettyPrinter;

    public BaseJsonSerializer(Jackson2ObjectMapperBuilder mapperBuilder) {
        this.mapper = mapperBuilder.build();
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Override
    public String serialize(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(T t, String indent) {
        if (indent.equals(DEFAULT_INDENT)) {
            return serialize(t);
        }

        DefaultPrettyPrinter.Indenter indenter =
                new DefaultIndenter(indent, DefaultIndenter.SYS_LF);
        prettyPrinter.indentObjectsWith(indenter);
        prettyPrinter.indentArraysWith(indenter);

        try {
            return mapper.writer(prettyPrinter).writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
