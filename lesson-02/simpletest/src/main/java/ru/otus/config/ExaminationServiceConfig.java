package ru.otus.config;

public record ExaminationServiceConfig(
        String inputNameTitle,
        String inputFirstNameLabel,
        String inputLastNameLabel,
        String inputAnswerLabel,
        int cancellationToken,
        String resultMessage,
        String passedMessage,
        String failedMessage
) {
}
