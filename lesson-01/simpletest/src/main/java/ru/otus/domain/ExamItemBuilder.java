package ru.otus.domain;

public interface ExamItemBuilder {

    ExamItemBuilder addAnswer(Answer answer) throws IllegalArgumentException;

    boolean tryAddAnswer(Answer answer);

    ExamItemBuilder setQuestion(String question);

    ExamItemBuilder setRightAnswerIndex(int rightAnswerIndex);

    ExamItem build();

    int getLastAnswerIndex();

    ExamItemBuilder createNewExamItemBuilder();
}