package ru.otus.service;

import ru.otus.domain.Exam;

public class ExamPrinterImplFactory {
    ExamPrinterImpl create(Exam exam) {
        if (exam == null) {
            return null;
        }
        return new ExamPrinterImpl(exam);
    }
}
