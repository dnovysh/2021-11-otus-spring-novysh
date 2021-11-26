package ru.otus.service;

import ru.otus.dao.ExamDAO;
import ru.otus.domain.Exam;

public class ExamServiceImpl implements ExamService {

    private final ExamDAO examDao;

    public ExamServiceImpl(ExamDAO examDao) {
        this.examDao = examDao;
    }

    @Override
    public Exam getExam() {
        return examDao.read();
    }
}
