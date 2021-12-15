package ru.otus.domain;

public record Author(
        int id,
        String firstName,
        String middleName,
        String lastName) {
}
