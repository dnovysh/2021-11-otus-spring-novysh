package ru.otus.service;

import ru.otus.dao.ExamDao;
import ru.otus.domain.Exam;

public class ExamServiceImpl implements ExamService {

    private final ExamDao examDao;

    public ExamServiceImpl(ExamDao examDao) {
        this.examDao = examDao;
    }

    @Override
    public Exam getExam() {
        return examDao.read();
    }
}
