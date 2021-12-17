package ru.otus.service;

import java.util.List;

public interface BaseSerializer<T> {

    String serialize(T t);

    String serialize(List<T> tList);

    String serialize(List<T> tList, String indent);
}
