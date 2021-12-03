package ru.otus.service;

import ru.otus.domain.Exam;

public interface ExamPrinterFactory {
    ExamPrinter create(Exam exam);
}
