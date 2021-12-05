package ru.otus.runner;

import lombok.val;
import ru.otus.service.ExamService;

public class ConsoleRunner implements Runner {

    private final ExamService examService;

    public ConsoleRunner(ExamService examService) {
        this.examService = examService;
    }

    @Override
    public void run() {
        val exam = examService.getExam();

        System.out.println("The whole test:\n");
        System.out.println(exam);

        System.out.println("\n\nThe third question:\n");
        System.out.println(exam.getExamItem(2));
    }
}
