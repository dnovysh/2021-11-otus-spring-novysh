package ru.otus.service;

import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;
import ru.otus.config.ApplicationConfig;
import ru.otus.domain.Person;
import ru.otus.domain.PersonExam;
import ru.otus.domain.PersonExamItemAnswer;
import ru.otus.domain.PersonExamResult;

import java.util.Locale;
import java.util.Scanner;

@Component
public class ConsoleExaminationService implements ExaminationService, MessageSourceAware {

    private final ExamService examService;
    private final ApplicationConfig applicationConfig;
    private MessageSource messageSource;

    public ConsoleExaminationService(ExamService examService, ApplicationConfig applicationConfig) {
        this.examService = examService;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void conductTest() {
        val exam = examService.getExam();
        Locale locale = applicationConfig.locale();

        Scanner in = new Scanner(System.in);

        System.out.println(System.lineSeparator());
        System.out.print(exam.getTitleBlock());

        Person person = getPersonInput(in, locale);
        val personExam = new PersonExam(person, exam);

        populateAnswersFromInput(in, personExam, locale);

        in.close();

        PersonExamResult result = personExam.calculateResult();

        printResult(person, result, locale);
    }

    protected int getIntInput(Scanner in, String label) {
        while (true) {
            System.out.print(label);

            if (in.hasNextInt()) {
                return in.nextInt();
            } else {
                in.next();
            }
        }
    }

    protected Person getPersonInput(Scanner in, Locale locale) {
        System.out.println(messageSource
                .getMessage("examination-service.input-name-title", null, locale));
        System.out.print(messageSource
                .getMessage("examination-service.input-first-name-label", null, locale));
        val firstName = in.next().trim();
        System.out.print(messageSource
                .getMessage("examination-service.input-last-name-label", null, locale));
        val lastName = in.next();

        return new Person(firstName, lastName);
    }

    protected void populateAnswersFromInput(Scanner in,
                                            PersonExam personExam,
                                            Locale locale) {

        int cancellationToken = Integer.parseInt(messageSource
                .getMessage("examination-service.cancellation-token", null, locale));

        for (PersonExamItemAnswer answer : personExam) {
            System.out.printf("\n\n%s\n", answer.getExamItem());

            int answerInput = getIntInput(in, messageSource
                    .getMessage("examination-service.input-answer-label",
                            new Object[]{cancellationToken}, locale));

            if (answerInput == cancellationToken) {
                break;
            }

            answer.setAnswer(answerInput);
        }
    }

    protected void printResult(Person person,
                               PersonExamResult result,
                               Locale locale) {
        System.out.printf("\n%s%s\n%s\n",
                person,
                messageSource.getMessage("examination-service.result-message",
                        new Object[]{result.actualPercentageOfCorrectAnswers()}, locale),
                result.passed()
                        ? messageSource.getMessage("examination-service.passed", null, locale)
                        : messageSource.getMessage("examination-service.failed", null, locale));
    }
}
