package ru.otus.domain;

import java.util.Objects;

public record PersonExamResult(
        int minPercentageOfCorrectAnswers,
        int actualPercentageOfCorrectAnswers,
        boolean passed) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonExamResult that = (PersonExamResult) o;
        return minPercentageOfCorrectAnswers == that.minPercentageOfCorrectAnswers &&
                actualPercentageOfCorrectAnswers == that.actualPercentageOfCorrectAnswers &&
                passed == that.passed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minPercentageOfCorrectAnswers, actualPercentageOfCorrectAnswers, passed);
    }
}
