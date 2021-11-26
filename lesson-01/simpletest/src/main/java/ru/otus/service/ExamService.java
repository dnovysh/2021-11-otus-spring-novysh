package ru.otus.service;

import com.opencsv.exceptions.CsvException;
import ru.otus.domain.Exam;

import java.io.IOException;

public interface ExamService {
    Exam getExam() throws IOException, CsvException;
}
