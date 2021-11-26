package ru.otus.service;

import com.opencsv.exceptions.CsvException;
import ru.otus.dao.ExamDAO;
import ru.otus.domain.Exam;

import java.io.IOException;

public class ExamServiceImpl implements ExamService {

    private final ExamDAO examDao;

    public ExamServiceImpl(ExamDAO examDao) {
        this.examDao = examDao;
    }

    @Override
    public Exam getExam() throws IOException, CsvException {
        return examDao.read();
    }
}
