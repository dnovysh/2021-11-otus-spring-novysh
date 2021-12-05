package ru.otus.domain;

public class ExamItemBuilderFactory {
    private final ExamItemBuilder template;

    public ExamItemBuilderFactory(ExamItemBuilder template) {
        this.template = template;
    }

    public ExamItemBuilder create() {
        return template.createNewExamItemBuilder();
    }
}
