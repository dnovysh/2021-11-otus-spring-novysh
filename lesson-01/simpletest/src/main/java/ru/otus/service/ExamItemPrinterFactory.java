package ru.otus.service;

import ru.otus.domain.ExamItem;

public interface ExamItemPrinterFactory {
    ExamItemPrinter create(ExamItem examItem);
}
