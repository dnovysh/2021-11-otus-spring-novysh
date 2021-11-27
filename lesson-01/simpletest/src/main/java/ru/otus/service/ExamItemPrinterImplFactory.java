package ru.otus.service;

import ru.otus.domain.ExamItem;

public class ExamItemPrinterImplFactory implements ExamItemPrinterFactory {
    public ExamItemPrinter create(ExamItem examItem) {
        if (examItem == null) {
            return null;
        }

        return new ExamItemPrinterImpl(examItem);
    }
}
