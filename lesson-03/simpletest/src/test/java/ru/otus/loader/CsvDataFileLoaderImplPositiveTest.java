package ru.otus.loader;

import lombok.val;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(value = CsvDataFileLoaderImpl.class)
@ActiveProfiles("test-csv-loader-positive")
class CsvDataFileLoaderImplPositiveTest {

    @Autowired
    private CsvDataFileLoaderImpl csvDataFileLoader;

    @Test
    @DisplayName("Method load should return expected data from the given file")
    void loadShouldReturnExpectedDataFromTheGivenFile() {
        //arrange
        List<String[]> expected = List.of(
                new String[]{"Sharon ____________ to San Fransisco last month. ",
                        " moves           ", " move          ", " moved <<+>>"},
                new String[]{"We ____________ out tonight.                     ",
                        " won’t go <<+>>  ", " not will go   ", " doesn’t go"},
                new String[]{"Where ____________ you born?                     ",
                        " was             ", " did           ", " were <<+>>"},
                new String[]{"Jake ______________ enjoy hiking at all.         ",
                        " do              ", " doesn’t <<+>> ", " don’t"},
                new String[]{"They ____________ to the seaside last year.      ",
                        " didn’t go <<+>> ", " weren’t go    ", " didn’t went"
                });

        // act
        List<String[]> loadedData;

        loadedData = csvDataFileLoader.load();

        val actual = loadedData;

        // assert
        assertAll("Comparing loaded data with expected data",
                () -> assertNotNull(actual),
                () -> assertEquals(expected.size(), actual.size()),
                () -> {
                    for (int i = 0, expectedSize = expected.size(); i < expectedSize; i++) {
                        String[] expectedRow = expected.get(i);
                        String[] actualRow = actual.get(i);
                        for (int j = 0, rowSize = expectedRow.length; j < rowSize; j++) {
                            assertEquals(expectedRow[j], actualRow[j]);
                        }
                    }
                });
    }
}
