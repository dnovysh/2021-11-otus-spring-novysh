package ru.otus.service;

import com.opencsv.exceptions.CsvException;
import ru.otus.dao.ExamDao;
import ru.otus.domain.Exam;

import java.io.IOException;

public class ExamServiceImpl implements ExamService {

    private final ExamDao examDao;

    public ExamServiceImpl(ExamDao examDao) {
        this.examDao = examDao;
    }

    @Override
    public Exam getExam() throws IOException, CsvException {
        return examDao.read();
    }
}
