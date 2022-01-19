package ru.otus.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonExam implements Iterable<PersonExamItemAnswer> {

    private static final int NO_ANSWER_YET = -1;

    @Getter
    private final Person person;
    @Getter
    private final Exam exam;
    private final List<PersonExamItemAnswer> personExamItemAnswers;

    public PersonExam(Person person, Exam exam) {
        this.person = person;
        this.exam = exam;
        this.personExamItemAnswers = new ArrayList<>();

        for (ExamItem examItem : exam) {
            this.personExamItemAnswers.add(new PersonExamItemAnswer(examItem, NO_ANSWER_YET));
        }
    }

    @Override
    public Iterator<PersonExamItemAnswer> iterator() {
        return personExamItemAnswers.iterator();
    }

    public PersonExamResult calculateResult() {
        int minPercentageOfCorrectAnswers = exam.getMinPercentageOfCorrectAnswers();

        int actualPercentageOfCorrectAnswers = (int) personExamItemAnswers.stream()
                .filter(PersonExamItemAnswer::isRight).count() * 100 / personExamItemAnswers.size();

        return new PersonExamResult(minPercentageOfCorrectAnswers,
                actualPercentageOfCorrectAnswers,
                actualPercentageOfCorrectAnswers >= minPercentageOfCorrectAnswers);
    }
}
