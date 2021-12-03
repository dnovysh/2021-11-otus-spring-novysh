package ru.otus.dao;

import com.opencsv.exceptions.CsvException;
import ru.otus.domain.Exam;

import java.io.IOException;

public interface ExamDao {
    Exam read() throws IOException, CsvException;
}
