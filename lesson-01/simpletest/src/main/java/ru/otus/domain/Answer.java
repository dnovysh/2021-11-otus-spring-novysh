package ru.otus.domain;

import lombok.Getter;

public class Answer {

    @Getter
    private final String text;

    public Answer(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
