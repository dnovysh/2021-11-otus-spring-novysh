package ru.otus.services.serializer;

public interface BaseSerializer<T> {

    String DEFAULT_INDENT = "  ";

    String serialize(T t);

    String serialize(T t, String indent);
}
