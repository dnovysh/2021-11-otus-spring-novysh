package ru.otus.service;

import ru.otus.domain.ExamItem;

public class ExamItemPrinterImpl implements ExamItemPrinter {

    private final ExamItem examItem;

    public ExamItemPrinterImpl(ExamItem examItem) {
        this.examItem = examItem;
    }

    @Override
    public void print() {
        System.out.print(examItem);
    }
}
