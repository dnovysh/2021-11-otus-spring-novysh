package ru.otus.service;

import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.config.ExaminationServiceConfig;
import ru.otus.domain.Person;
import ru.otus.domain.PersonExam;
import ru.otus.domain.PersonExamItemAnswer;
import ru.otus.domain.PersonExamResult;

import java.util.Scanner;

@Component
public class ConsoleExaminationService implements ExaminationService {

    private final ExamService examService;
    private final ExaminationServiceConfig examinationServiceConfig;

    public ConsoleExaminationService(ExamService examService,
                                     ExaminationServiceConfig examinationServiceConfig) {
        this.examService = examService;
        this.examinationServiceConfig = examinationServiceConfig;
    }

    @Override
    public void conductTest() {
        val exam = examService.getExam();
        Scanner in = new Scanner(System.in);

        System.out.println(System.lineSeparator());
        System.out.print(exam.getTitleBlock());

        Person person = getPersonInput(in, examinationServiceConfig);
        val personExam = new PersonExam(person, exam);

        populateAnswersFromInput(in, personExam, examinationServiceConfig);

        in.close();

        PersonExamResult result = personExam.calculateResult();

        printResult(person, result, examinationServiceConfig);
    }

    private int getIntInput(Scanner in, String label) {
        while (true) {
            System.out.print(label);

            if (in.hasNextInt()) {
                return in.nextInt();
            } else {
                in.next();
            }
        }
    }

    private Person getPersonInput(Scanner in, ExaminationServiceConfig serviceConfig) {
        System.out.println(serviceConfig.inputNameTitle());
        System.out.print(serviceConfig.inputFirstNameLabel());
        val firstName = in.next().trim();
        System.out.print(serviceConfig.inputLastNameLabel());
        val lastName = in.next();

        return new Person(firstName, lastName);
    }

    private void populateAnswersFromInput(Scanner in,
                                          PersonExam personExam,
                                          ExaminationServiceConfig serviceConfig) {
        for (PersonExamItemAnswer answer : personExam) {
            System.out.printf("\n\n%s\n", answer.getExamItem());

            int answerInput = getIntInput(in, serviceConfig.inputAnswerLabel());

            if (answerInput == serviceConfig.cancellationToken()) {
                break;
            }

            answer.setAnswer(answerInput);
        }
    }

    private void printResult(Person person,
                             PersonExamResult result,
                             ExaminationServiceConfig serviceConfig) {
        System.out.printf("\n%s%s%s%%\n%s\n",
                person,
                serviceConfig.resultMessage(),
                result.actualPercentageOfCorrectAnswers(),
                result.passed() ? serviceConfig.passedMessage() : serviceConfig.failedMessage());
    }
}
