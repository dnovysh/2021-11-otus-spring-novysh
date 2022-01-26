package ru.otus.domain;

public record Answer(String text) {

    @Override
    public String toString() {
        return text;
    }
}
