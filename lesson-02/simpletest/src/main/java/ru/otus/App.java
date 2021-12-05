package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.runner.Runner;

public class App {
    public static void main(String[] args) {
        final ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");

        final Runner runner = context.getBean(Runner.class);

        runner.run();

        context.close();
    }
}
