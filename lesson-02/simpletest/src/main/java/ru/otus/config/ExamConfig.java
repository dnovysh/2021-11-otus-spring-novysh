package ru.otus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:exam.properties")
public record ExamConfig(String title,
                         String minPercentageOfCorrectAnswersLabel,
                         int minPercentageOfCorrectAnswers,
                         String rightAnswerToken) {

    public ExamConfig(@Value("${exam-config.title}") String title,
                      @Value("${exam-config.min-percentage-of-correct-answers-label}")
                              String minPercentageOfCorrectAnswersLabel,
                      @Value("${exam-config.min-percentage-of-correct-answers}")
                              int minPercentageOfCorrectAnswers,
                      @Value("${exam-config.right-answer-token}") String rightAnswerToken) {
        this.title = (title == null) ? "" : title;
        this.minPercentageOfCorrectAnswersLabel = minPercentageOfCorrectAnswersLabel;
        this.minPercentageOfCorrectAnswers = minPercentageOfCorrectAnswers;
        this.rightAnswerToken = rightAnswerToken;
    }
}
