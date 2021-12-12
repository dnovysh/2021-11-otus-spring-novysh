package ru.otus.domain;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.ExamDao;
import ru.otus.dao.ExamDaoImpl;
import ru.otus.loader.CsvDataFileLoaderImpl;
import ru.otus.loader.ResourceFileAsStringLoaderImpl;
import ru.otus.service.ExamServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

class ExamIntegrationTest {

    @Test
    @DisplayName("Exam integration test")
    void testToString() {
        String expected = "Simple tenses test\n" +
                "\n" +
                "Minimum percentage of correct answers: 80%\n" +
                "\n" +
                "Sharon ____________ to San Fransisco last month.\n" +
                "\n" +
                "1 moves\n" +
                "2 move\n" +
                "3 moved\n" +
                "\n" +
                "We ____________ out tonight.\n" +
                "\n" +
                "1 won’t go\n" +
                "2 not will go\n" +
                "3 doesn’t go\n" +
                "\n" +
                "Where ____________ you born?\n" +
                "\n" +
                "1 was\n" +
                "2 did\n" +
                "3 were\n" +
                "\n" +
                "Jake ______________ enjoy hiking at all.\n" +
                "\n" +
                "1 do\n" +
                "2 doesn’t\n" +
                "3 don’t\n" +
                "\n" +
                "They ____________ to the seaside last year.\n" +
                "\n" +
                "1 didn’t go\n" +
                "2 weren’t go\n" +
                "3 didn’t went\n";

        Exam exam;

        var resourceFileAsStringLoader = new ResourceFileAsStringLoaderImpl(
                "/data/simple-tenses-test-header.json");

        var csvDataFileLoader = new CsvDataFileLoaderImpl(
                "/data/simple-tenses-test-items.csv", '|');

        ExamDao examDao = new ExamDaoImpl(resourceFileAsStringLoader, csvDataFileLoader);

        exam = new ExamServiceImpl(examDao).getExam();

        val actualExam = exam;

        assertAll("Comparing the loaded Exam in string representation with the expected one",
                () -> assertNotNull(actualExam),
                () -> assertEquals(expected, actualExam.toString()));
    }
}