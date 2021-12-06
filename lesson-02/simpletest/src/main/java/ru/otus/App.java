package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.service.ExaminationService;

public class App {
    public static void main(String[] args) {
        final ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");

        final ExaminationService consoleExaminationService =
                context.getBean(ExaminationService.class);

        consoleExaminationService.conductTest();

        context.close();
    }
}
