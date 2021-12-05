package ru.otus.service;

import ru.otus.domain.Exam;

public class ExamPrinterImpl implements ExamPrinter {

    private final Exam exam;

    public ExamPrinterImpl(Exam exam) {
        this.exam = exam;
    }

    @Override
    public void print() {
        System.out.print(exam);
    }
}
