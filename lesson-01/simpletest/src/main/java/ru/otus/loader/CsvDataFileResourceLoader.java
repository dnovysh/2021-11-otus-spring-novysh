package ru.otus.loader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.var;
import ru.otus.dao.ExamDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class CsvDataFileResourceLoader implements CsvDataFileLoader {

    private final String fileName;
    private final CSVParser parser;

    public CsvDataFileResourceLoader(String fileName, char separator) {
        this.fileName = fileName;
        this.parser = new CSVParserBuilder().withSeparator(separator).build();
    }

    @Override
    public List<String[]> load() throws IOException, CsvException {
        try (var br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(fileName)), StandardCharsets.UTF_8));
             var reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {

            return reader.readAll();
        }
    }
}
