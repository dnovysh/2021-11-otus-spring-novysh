package ru.otus.dao;

import com.opencsv.exceptions.CsvException;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import ru.otus.domain.*;
import ru.otus.loader.CsvDataFileLoader;

import java.io.IOException;

public class ExamDaoImpl implements ExamDao {

    private final String title;
    private final CsvDataFileLoader csvDataFileLoader;
    private final String rightAnswerToken;
    private final ExamBuilderFactory examBuilderFactory;
    private final ExamItemBuilderFactory examItemBuilderFactory;

    public ExamDaoImpl(String title, CsvDataFileLoader csvDataFileLoader, String rightAnswerToken,
                       ExamBuilderFactory examBuilderFactory, ExamItemBuilderFactory examItemBuilderFactory) {
        this.title = title;
        this.csvDataFileLoader = csvDataFileLoader;
        this.rightAnswerToken = rightAnswerToken;
        this.examBuilderFactory = examBuilderFactory;
        this.examItemBuilderFactory = examItemBuilderFactory;
    }

    @Override
    public Exam read() throws IOException, CsvException {
        var examBuilder = examBuilderFactory.create();
        examBuilder.setTitle(title);

        var rows = csvDataFileLoader.load();

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

                var answerHasBeenAdded = examItemBuilder.tryAddAnswer(new Answer(answerText));

                if (answerHasBeenAdded && containsRightAnswerToken) {
                    examItemBuilder.setRightAnswerIndex(examItemBuilder.getLastAnswerIndex());
                }
            }

            var examItem = examItemBuilder.build();
            examBuilder.tryAddExamItem(examItem);
        }

        return examBuilder.build();
    }
}
