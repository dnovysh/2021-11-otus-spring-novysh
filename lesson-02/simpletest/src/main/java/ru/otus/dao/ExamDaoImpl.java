package ru.otus.dao;

import org.apache.commons.lang3.StringUtils;
import ru.otus.config.ExamConfig;
import ru.otus.domain.Answer;
import ru.otus.domain.Exam;
import ru.otus.domain.ExamItem;
import ru.otus.loader.CsvDataFileLoader;

import java.util.ArrayList;
import java.util.Arrays;

public class ExamDaoImpl implements ExamDao {

    private static final int NO_RIGHT_ANSWER_INDEX = -1;

    private final ExamConfig examConfig;
    private final CsvDataFileLoader csvDataFileLoader;

    public ExamDaoImpl(ExamConfig examConfig, CsvDataFileLoader csvDataFileLoader) {
        this.examConfig = examConfig;
        this.csvDataFileLoader = csvDataFileLoader;
    }

    @Override
    public Exam read() {

        var rows = csvDataFileLoader.load();

        var examItems = new ArrayList<ExamItem>();

        for (String[] row : rows) {

            String question = "";
            var answers = new ArrayList<Answer>();
            int rightAnswerIndex = NO_RIGHT_ANSWER_INDEX;


            for (int i = 0, rowLength = row.length; i < rowLength; i++) {

                String columnValue = row[i].trim();

                if (StringUtils.isBlank(columnValue)) {
                    continue;
                }

                if (i == 0) {
                    question = columnValue;
                    continue;
                }

                var containsRightAnswerToken = columnValue.contains(examConfig.rightAnswerToken());
                var answerText = containsRightAnswerToken
                        ? columnValue.replace(examConfig.rightAnswerToken(), "").trim()
                        : columnValue;

                if (StringUtils.isBlank(answerText)) {
                    continue;
                }

                answers.add(new Answer(answerText));

                if (containsRightAnswerToken) {
                    rightAnswerIndex = answers.size() - 1;
                }
            }

            if (StringUtils.isBlank(question)) {
                throw new RuntimeException(String.format("The question is not specified in the test item line:\n%s\n",
                        Arrays.toString(row)));
            }

            if (answers.isEmpty()) {
                throw new RuntimeException(String.format("There are no answers in the test item line:\n%s\n",
                        Arrays.toString(row)));
            }

            if (rightAnswerIndex == NO_RIGHT_ANSWER_INDEX) {
                throw new RuntimeException(
                        String.format("There is no correct answer in the test item line:\n%s\n" +
                                        "right answer token:%s\n",
                                Arrays.toString(row), examConfig.rightAnswerToken()));
            }

            examItems.add(new ExamItem(question, answers, rightAnswerIndex));
        }

        if (examItems.isEmpty()) {
            throw new RuntimeException("There are no tasks in the test");
        }

        return new Exam(examConfig.title(),
                examConfig.minPercentageOfCorrectAnswersLabel(),
                examConfig.minPercentageOfCorrectAnswers(),
                examItems);
    }
}
