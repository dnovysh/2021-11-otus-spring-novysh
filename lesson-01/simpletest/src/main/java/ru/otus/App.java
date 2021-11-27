package ru.otus;

import com.opencsv.exceptions.CsvException;
import lombok.val;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.service.ExamItemPrinterFactory;
import ru.otus.service.ExamPrinterFactory;
import ru.otus.service.ExamService;

import java.io.IOException;

public class App
{
    public static void main( String[] args ) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");

        ExamService examService = context.getBean(ExamService.class);
        ExamPrinterFactory examPrinterFactory = context.getBean(ExamPrinterFactory.class);
        ExamItemPrinterFactory examItemPrinterFactory = context.getBean(ExamItemPrinterFactory.class);

        try {
            val exam = examService.getExam();

            System.out.println("The whole test:\n");
            val examPrinter = examPrinterFactory.create(exam);
            examPrinter.print();

            System.out.println("\n\n\nThe third question:\n");
            val examItemPrinter = examItemPrinterFactory.create(exam.getExamItem(2));
            examItemPrinter.print();

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        context.close();
    }
}
