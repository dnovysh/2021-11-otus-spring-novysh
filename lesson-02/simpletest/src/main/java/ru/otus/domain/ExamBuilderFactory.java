package ru.otus.domain;

public class ExamBuilderFactory {

    private final ExamBuilder template;

    public ExamBuilderFactory(ExamBuilder template) {
        this.template = template;
    }

    public ExamBuilder create() {
        return template.createNewExamBuilder();
    }
}
