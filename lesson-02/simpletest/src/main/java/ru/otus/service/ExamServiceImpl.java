package ru.otus.service;

import org.springframework.stereotype.Component;
import ru.otus.dao.ExamDao;
import ru.otus.domain.Exam;

@Component
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
