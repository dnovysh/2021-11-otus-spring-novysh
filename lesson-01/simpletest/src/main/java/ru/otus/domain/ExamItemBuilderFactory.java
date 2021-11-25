package ru.otus.domain;

public class ExamItemBuilderFactory {
    private final ExamItemBuilder impl;

    public ExamItemBuilderFactory(ExamItemBuilder impl) {
        this.impl = impl;
    }

    public ExamItemBuilder create(){
        return impl.createNewExamItemBuilder();
    }
}
