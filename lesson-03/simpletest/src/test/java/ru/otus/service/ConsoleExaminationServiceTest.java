package ru.otus.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.config.ApplicationConfig;
import ru.otus.dao.ExamDaoImpl;
import ru.otus.loader.CsvDataFileLoaderImpl;
import ru.otus.loader.ResourceFileAsStringLoaderImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties({
        ResourceFileAsStringLoaderImpl.class,
        CsvDataFileLoaderImpl.class,
        ApplicationConfig.class})
@Import({ResourceBundleMessageSource.class, ExamDaoImpl.class, ExamServiceImpl.class})
@ActiveProfiles("examination-service-integration-test")
@DisplayName("Console Examination Service should ask first name, last name, right answers for questions and")
class ConsoleExaminationServiceTest {
    @Autowired
    MessageSource messageSource;
    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    private ExamService examService;

    static Stream<Arguments> failedResultArgumentProvider() {
        return Stream.of(
                arguments("Foo\nBar\n2\n3\n3\n1\n2\n", "/result/failed20percent.txt"),
                arguments("Foo\nBar\n2\n1\n1\n3\n1\n", "/result/failed40percent.txt"),
                arguments("Foo\nBar\n2\n1\n3\n2\n3\n", "/result/failed60percent.txt")
        );
    }

    static Stream<Arguments> passedResultArgumentProvider() {
        return Stream.of(
                arguments("Foo\nBar\n3\n2\n3\n2\n1\n", "/result/passed80percent.txt"),
                arguments("Foo\nBar\n3\n1\n3\n2\n1\n", "/result/passed100percent.txt"),
                arguments("Foo\nBar\n3\n1\nstr\n3\n2\n1\n", "/result/passedstr100percent.txt")
        );
    }

    @DisplayName("print test failed result")
    @ParameterizedTest
    @MethodSource("failedResultArgumentProvider")
    void conductTestShouldPrintTestFailedResult(String userInput, String resultFileName) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var inputStream = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
        var outputStream = new PrintStream(byteArrayOutputStream);
        var examinationService = new ConsoleExaminationService(
                examService, applicationConfig, inputStream, outputStream
        );
        examinationService.setMessageSource(messageSource);
        examinationService.conductTest();
        var actual = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        var expected = new ResourceFileAsStringLoaderImpl(resultFileName).load();
        assertEquals(expected, actual);
    }

    @DisplayName("print test passed result")
    @ParameterizedTest
    @MethodSource("passedResultArgumentProvider")
    void conductTestShouldPrintPassedResult(String userInput, String resultFileName) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var inputStream = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
        var outputStream = new PrintStream(byteArrayOutputStream);
        var examinationService = new ConsoleExaminationService(
                examService, applicationConfig, inputStream, outputStream
        );
        examinationService.setMessageSource(messageSource);
        examinationService.conductTest();
        var actual = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        var expected = new ResourceFileAsStringLoaderImpl(resultFileName).load();
        assertEquals(expected, actual);
    }
}
