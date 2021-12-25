package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

public record Author(
        int id,
        String firstName,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String middleName,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String lastName) {
}
