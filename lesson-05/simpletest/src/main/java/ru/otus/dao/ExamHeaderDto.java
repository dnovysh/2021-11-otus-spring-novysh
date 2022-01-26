package ru.otus.dao;

public record ExamHeaderDto(
        String title,
        String minPercentageOfCorrectAnswersLabel,
        int minPercentageOfCorrectAnswers,
        String rightAnswerToken) {
}
