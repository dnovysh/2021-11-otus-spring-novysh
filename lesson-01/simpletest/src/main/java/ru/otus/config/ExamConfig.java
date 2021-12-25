package ru.otus.config;

import lombok.Getter;

public class ExamConfig {

    @Getter
    private final String title;
    @Getter
    private final String minPercentageOfCorrectAnswersLabel;
    @Getter
    private final int minPercentageOfCorrectAnswers;
    @Getter
    private final String rightAnswerToken;

    public ExamConfig(String title, String minPercentageOfCorrectAnswersLabel,
                      int minPercentageOfCorrectAnswers, String rightAnswerToken) {
        this.title = (title == null) ? "" : title;
        this.minPercentageOfCorrectAnswersLabel = minPercentageOfCorrectAnswersLabel;
        this.minPercentageOfCorrectAnswers = minPercentageOfCorrectAnswers;
        this.rightAnswerToken = rightAnswerToken;
    }
}
