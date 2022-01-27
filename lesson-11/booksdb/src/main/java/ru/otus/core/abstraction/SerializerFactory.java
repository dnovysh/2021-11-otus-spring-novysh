package ru.otus.core.abstraction;

public interface SerializerFactory<T> {
    BaseSerializer<T> getSerializer();
}
