package ru.otus.domain;

public interface ExamBuilder {

    ExamBuilder setTitle(String title);

    ExamBuilder addExamItem(ExamItem examItem);

    Exam build();

    ExamBuilder createNewExamBuilder();
}
