package ru.otus.loader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(value = CsvDataFileLoaderImpl.class)
@ActiveProfiles("test-csv-loader-negative")
class CsvDataFileLoaderImplNegativeTest {

    @Autowired
    private CsvDataFileLoaderImpl csvDataFileLoader;

    @Test
    @DisplayName("Method load should raise NullPointerException if data file is not found")
    void loadShouldRaiseIOExceptionIfDataFileIsNotFound() {
        assertThrows(NullPointerException.class, csvDataFileLoader::load);
    }

}