package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.config.ApplicationConfig;
import ru.otus.loader.CsvDataFileLoaderImpl;
import ru.otus.loader.ResourceFileAsStringLoaderImpl;
import ru.otus.service.ExaminationService;

@SpringBootApplication
@EnableConfigurationProperties({
        ApplicationConfig.class,
        ResourceFileAsStringLoaderImpl.class,
        CsvDataFileLoaderImpl.class
})
public class SimpletestApplication {

    public static void main(String[] args) {

        final var context = SpringApplication.run(SimpletestApplication.class, args);

        ExaminationService consoleExaminationService = context.getBean(ExaminationService.class);

        consoleExaminationService.conductTest();

    }
}
