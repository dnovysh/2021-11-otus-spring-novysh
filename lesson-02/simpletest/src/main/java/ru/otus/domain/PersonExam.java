package ru.otus.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonExam implements Iterable<ExamItemPersonAnswer>{

    private static final int NO_ANSWER_YET = -1;

    @Getter
    private final Person person;
    @Getter
    private final Exam exam;
    private final List<ExamItemPersonAnswer> examItemPersonAnswers;

    public PersonExam(Person person, Exam exam) {
        this.person = person;
        this.exam = exam;
        this.examItemPersonAnswers = new ArrayList<>();

        for (ExamItem examItem : exam) {
            this.examItemPersonAnswers.add(new ExamItemPersonAnswer(examItem, NO_ANSWER_YET));
        }
    }

    @Override
    public Iterator<ExamItemPersonAnswer> iterator() {
        return new Iterator<>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < examItemPersonAnswers.size();
            }

            @Override
            public ExamItemPersonAnswer next() {
                return examItemPersonAnswers.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
