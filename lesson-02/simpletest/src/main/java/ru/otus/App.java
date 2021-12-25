package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.service.ExaminationService;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        ExaminationService consoleExaminationService = context.getBean(ExaminationService.class);

        consoleExaminationService.conductTest();

        context.close();
    }
}
