package ru.otus.domain;

public record Person(String firstName, String lastName) {
    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }
}
