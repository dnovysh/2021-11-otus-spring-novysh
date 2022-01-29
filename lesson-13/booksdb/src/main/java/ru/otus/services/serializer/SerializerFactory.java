package ru.otus.services.serializer;

public interface SerializerFactory<T> {
    BaseSerializer<T> getSerializer();
}
