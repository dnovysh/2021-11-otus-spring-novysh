package ru.otus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:examination-service.properties")
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
    public ExaminationServiceConfig(
            @Value("${examination-service.input-name-title}") String inputNameTitle,
            @Value("${examination-service.input-first-name-label}") String inputFirstNameLabel,
            @Value("${examination-service.input-last-name-label}") String inputLastNameLabel,
            @Value("${examination-service.input-answer-label}") String inputAnswerLabel,
            @Value("${examination-service.cancellation-token}") int cancellationToken,
            @Value("${examination-service.result-message}") String resultMessage,
            @Value("${examination-service.passed}") String passedMessage,
            @Value("${examination-service.failed}") String failedMessage) {
        this.inputNameTitle = inputNameTitle;
        this.inputFirstNameLabel = inputFirstNameLabel;
        this.inputLastNameLabel = inputLastNameLabel;
        this.inputAnswerLabel = inputAnswerLabel;
        this.cancellationToken = cancellationToken;
        this.resultMessage = resultMessage;
        this.passedMessage = passedMessage;
        this.failedMessage = failedMessage;
    }
}
