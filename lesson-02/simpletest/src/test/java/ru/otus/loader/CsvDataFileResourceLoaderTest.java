package ru.otus.loader;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CSV data file loader test")
class CsvDataFileResourceLoaderTest {

    @Test
    @DisplayName("Method load should raise NullPointerException if data file is not found")
    void loadShouldRaiseIOExceptionIfDataFileIsNotFound() {
        //arrange
        var csvDataFileLoader = new CsvDataFileResourceLoader("/data/not-existing-file.csv", '|');

        //act & assert
        assertThrows(NullPointerException.class, csvDataFileLoader::load);
    }

    @Test
    @DisplayName("Method load should return expected data from the given file")
    void loadShouldReturnExpectedDataFromTheGivenFile() {
        //arrange
        var csvDataFileLoader = new CsvDataFileResourceLoader("/data/simple-tenses-test.csv", '|');

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