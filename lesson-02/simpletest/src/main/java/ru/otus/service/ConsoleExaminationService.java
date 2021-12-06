package ru.otus.service;

import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.config.ExaminationServiceConfig;
import ru.otus.domain.ExamItemPersonAnswer;
import ru.otus.domain.Person;
import ru.otus.domain.PersonExam;

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

    public void conductTest() {
        val exam = examService.getExam();
        Scanner in = new Scanner(System.in);

        System.out.print(exam.getTitleBlock());

        System.out.println(examinationServiceConfig.inputNameTitle());
        System.out.print(examinationServiceConfig.inputFirstNameLabel());
        val firstName = in.next().trim();
        System.out.print(examinationServiceConfig.inputLastNameLabel());
        val lastName = in.next();

        val person = new Person(firstName, lastName);
        val personExam = new PersonExam(person, exam);

        for (ExamItemPersonAnswer answer : personExam) {
            System.out.printf("\n\n%s\n", answer.getExamItem());

            int answerInput = getIntInput(in, examinationServiceConfig.inputAnswerLabel());

            if (answerInput == examinationServiceConfig.cancellationToken()) {
                break;
            }

            answer.setAnswer(answerInput);
        }

        in.close();

        int percentageOfCorrectAnswers = personExam.calculatePercentageOfCorrectAnswers();
        int minPercentageOfCorrectAnswers = personExam.getExam().getMinPercentageOfCorrectAnswers();
        boolean testPassed = percentageOfCorrectAnswers >= minPercentageOfCorrectAnswers;

        System.out.printf("\n%s%s%s%%\n%s\n", personExam.getPerson(),
                examinationServiceConfig.resultMessage(),
                percentageOfCorrectAnswers,
                testPassed
                        ? examinationServiceConfig.passedMessage()
                        : examinationServiceConfig.failedMessage());
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
}
