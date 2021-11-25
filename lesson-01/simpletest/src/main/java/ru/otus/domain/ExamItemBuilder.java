package ru.otus.domain;

import java.util.ArrayList;

public interface ExamItemBuilder {

    ExamItemBuilder addAnswer(Answer answer);

    ExamItemBuilder setQuestion(String question);

    ExamItemBuilder setRightAnswerIndex(int rightAnswerIndex);

    ExamItem build();

    ExamItemBuilder createNewExamItemBuilder();
}
