package ru.otus.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import ru.otus.annotation.ArraySource;
import ru.otus.annotation.ArraySources;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonExamTest {

    @DisplayName("Should calculate correct result for given user answers")
    @ParameterizedTest
    @ArraySources(
            arrays = {
                    @ArraySource(array = {1, 100, 3, 7, 5, 5, 2}),
                    @ArraySource(array = {0, 20, 8, 4, 3, 5, 6}),
                    @ArraySource(array = {1, 60, 2, 7, 5, 9, 2})
            }
    )
    void calculateResult(int[] userAnswerIndexesWithExpectedResult) {
        var person = new Person("Test", "User");
        var emptyAnswers = new ArrayList<Answer>();

        List<ExamItem> examItems = new ArrayList<>(List.of(
                new ExamItem("Question 1", emptyAnswers, 3),
                new ExamItem("Question 2", emptyAnswers, 7),
                new ExamItem("Question 3", emptyAnswers, 5),
                new ExamItem("Question 4", emptyAnswers, 5),
                new ExamItem("Question 5", emptyAnswers, 2)
        ));

        var exam = new Exam("Test exam", "Min percentage", 60, examItems);

        var personExam = new PersonExam(person, exam);

        int i = 2;
        for (PersonExamItemAnswer answer : personExam) {
            answer.setAnswer(userAnswerIndexesWithExpectedResult[i++] + 1);
        }

        PersonExamResult personExamResult = personExam.calculateResult();
        boolean expected = userAnswerIndexesWithExpectedResult[0] == 1;
        int expectedPercent = userAnswerIndexesWithExpectedResult[1];

        assertEquals(expected, personExamResult.passed());
        assertEquals(expectedPercent, personExamResult.actualPercentageOfCorrectAnswers());
    }
}
