package ru.otus.config;

public record ExamConfig(String title,
                         String minPercentageOfCorrectAnswersLabel,
                         int minPercentageOfCorrectAnswers,
                         String rightAnswerToken) {

    public ExamConfig(String title, String minPercentageOfCorrectAnswersLabel,
                      int minPercentageOfCorrectAnswers, String rightAnswerToken) {
        this.title = (title == null) ? "" : title;
        this.minPercentageOfCorrectAnswersLabel = minPercentageOfCorrectAnswersLabel;
        this.minPercentageOfCorrectAnswers = minPercentageOfCorrectAnswers;
        this.rightAnswerToken = rightAnswerToken;
    }
}
