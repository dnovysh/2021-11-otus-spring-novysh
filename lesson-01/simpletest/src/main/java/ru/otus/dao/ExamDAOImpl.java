package ru.otus.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import ru.otus.domain.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExamDAOImpl implements ExamDAO {

    private final Path path;
    private final CSVParser parser;
    private final String rightAnswerToken;
    private final ExamBuilderFactory examBuilderFactory;
    private final ExamItemBuilderFactory examItemBuilderFactory;

    public ExamDAOImpl(String fileName, char separator, String rightAnswerToken,
                       ExamBuilderFactory examBuilderFactory, ExamItemBuilderFactory examItemBuilderFactory) {
        this.path = Paths.get(fileName);
        this.parser = new CSVParserBuilder().withSeparator(separator).build();
        this.rightAnswerToken = rightAnswerToken;
        this.examBuilderFactory = examBuilderFactory;
        this.examItemBuilderFactory = examItemBuilderFactory;
    }

    @Override
    public Exam read() throws IOException, CsvException {
        var examBuilder = examBuilderFactory.create();

        try (var br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             var reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {

            List<String[]> rows = reader.readAll();

            for (String[] row : rows) {
                var examItemBuilder = examItemBuilderFactory.create();

                for (int i = 0, rowLength = row.length; i < rowLength; i++) {
                    String columnValue = row[i].trim();

                    if (StringUtils.isBlank(columnValue)) {
                        continue;
                    }

                    if (i == 0) {
                        examItemBuilder.setQuestion(columnValue);
                        continue;
                    }

                    var containsRightAnswerToken = columnValue.contains(rightAnswerToken);
                    var answerText = containsRightAnswerToken
                            ? columnValue.replace(rightAnswerToken, "").trim() : columnValue;

                    if (StringUtils.isBlank(answerText)) {
                        continue;
                    }

                    examItemBuilder.addAnswer(new Answer(answerText));

                    if (containsRightAnswerToken) {
                        examItemBuilder.setRightAnswerIndex(examItemBuilder.getLastAnswerIndex());
                    }
                }

                var examItem = examItemBuilder.build();

                if (examItem != null) {
                    examBuilder.addExamItem(examItem);
                }
            }
        }

        return examBuilder.build();
    }
}
