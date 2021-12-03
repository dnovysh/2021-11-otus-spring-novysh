package ru.otus.loader;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.List;

public interface CsvDataFileLoader {
    List<String[]> load() throws IOException, CsvException;
}
