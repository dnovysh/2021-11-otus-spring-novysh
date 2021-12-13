package ru.otus.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.dao.ExamDaoImpl;
import ru.otus.loader.CsvDataFileLoaderImpl;
import ru.otus.loader.ResourceFileAsStringLoaderImpl;
import ru.otus.service.ExamServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties({
        ResourceFileAsStringLoaderImpl.class,
        CsvDataFileLoaderImpl.class})
@Import({ExamDaoImpl.class, ExamServiceImpl.class})
@ActiveProfiles("exam-integration-test")
class ExamIntegrationTest {

    @Autowired
    private ExamServiceImpl examService;

    @Test
    @DisplayName("Exam integration test")
    void testToString() {
        String expected = "Simple tenses test\n" +
                "\n" + "Minimum percentage of correct answers: 80%\n" + "\n" +
                "Sharon ____________ to San Fransisco last month.\n" + "\n" +
                "1 moves\n" + "2 move\n" + "3 moved\n" + "\n" +
                "We ____________ out tonight.\n" + "\n" +
                "1 won’t go\n" + "2 not will go\n" + "3 doesn’t go\n" + "\n" +
                "Where ____________ you born?\n" + "\n" +
                "1 was\n" + "2 did\n" + "3 were\n" + "\n" +
                "Jake ______________ enjoy hiking at all.\n" + "\n" +
                "1 do\n" + "2 doesn’t\n" + "3 don’t\n" + "\n" +
                "They ____________ to the seaside last year.\n" + "\n" +
                "1 didn’t go\n" + "2 weren’t go\n" + "3 didn’t went\n";

        Exam actualExam = examService.getExam();

        assertAll("Comparing the loaded Exam in string representation with the expected one",
                () -> assertNotNull(actualExam),
                () -> assertEquals(expected, actualExam.toString()));
    }
}