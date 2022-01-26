package ru.otus.domain;

import lombok.Getter;

public class PersonExamItemAnswer {
    @Getter
    private final ExamItem examItem;
    @Getter
    private int answerPosition;
    @Getter
    private boolean isRight;

    public PersonExamItemAnswer(ExamItem examItem, int answerPosition) {
        this.examItem = examItem;
        setAnswer(answerPosition);
    }

    public void setAnswer(int answerPosition) {
        this.answerPosition = answerPosition;
        this.isRight = this.examItem.answerIsRight(this.answerPosition);
    }
}
