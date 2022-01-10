package ru.otus.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;
import ru.otus.config.ApplicationConfig;
import ru.otus.domain.Person;
import ru.otus.domain.PersonExam;
import ru.otus.domain.PersonExamItemAnswer;
import ru.otus.domain.PersonExamResult;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

@Component
public class ConsoleExaminationService implements ExaminationService, MessageSourceAware {

    private final ExamService examService;
    private final ApplicationConfig applicationConfig;
    private final InputStream inputStream;
    private final PrintStream printStream;
    private MessageSource messageSource;

    public ConsoleExaminationService(ExamService examService, ApplicationConfig applicationConfig,
                                     @Value("#{ T(java.lang.System).in}") InputStream inputStream,
                                     @Value("#{ T(java.lang.System).out}") PrintStream printStream) {
        this.examService = examService;
        this.applicationConfig = applicationConfig;
        this.inputStream = inputStream;
        this.printStream = printStream;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void conductTest() {
        val exam = examService.getExam();
        Locale locale = applicationConfig.locale();
        Scanner in = new Scanner(inputStream);
        printStream.print("\n\n");
        printStream.print(exam.getTitleBlock());
        Person person = getPersonInput(in, printStream, locale);
        val personExam = new PersonExam(person, exam);
        populateAnswersFromInput(in, printStream, personExam, locale);
        in.close();
        PersonExamResult result = personExam.calculateResult();
        printResult(printStream, person, result, locale);
    }

    private int getIntInput(Scanner in, String label) {
        while (true) {
            printStream.print(label);

            if (in.hasNextInt()) {
                return in.nextInt();
            } else {
                in.next();
            }
        }
    }

    private Person getPersonInput(Scanner in, PrintStream printStream, Locale locale) {
        printStream.print(messageSource
                .getMessage("examination-service.input-name-title", null, locale) + "\n");
        printStream.print(messageSource
                .getMessage("examination-service.input-first-name-label", null, locale));
        val firstName = in.next().trim();
        printStream.print(messageSource
                .getMessage("examination-service.input-last-name-label", null, locale));
        val lastName = in.next();

        return new Person(firstName, lastName);
    }

    private void populateAnswersFromInput(Scanner in,
                                          PrintStream printStream,
                                          PersonExam personExam,
                                          Locale locale) {

        int cancellationToken = Integer.parseInt(messageSource
                .getMessage("examination-service.cancellation-token", null, locale));

        for (PersonExamItemAnswer answer : personExam) {
            printStream.printf("\n\n%s\n", answer.getExamItem());

            int answerInput = getIntInput(in, messageSource
                    .getMessage("examination-service.input-answer-label",
                            new Object[]{cancellationToken}, locale));

            if (answerInput == cancellationToken) {
                break;
            }

            answer.setAnswer(answerInput);
        }
    }

    private void printResult(PrintStream printStream,
                             Person person,
                             PersonExamResult result,
                             Locale locale) {
        printStream.printf("\n%s%s\n%s\n",
                person,
                messageSource.getMessage("examination-service.result-message",
                        new Object[]{result.actualPercentageOfCorrectAnswers()}, locale),
                result.passed()
                        ? messageSource.getMessage("examination-service.passed", null, locale)
                        : messageSource.getMessage("examination-service.failed", null, locale));
    }
}
