package ru.otus.dao;

import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.StringUtils;
import ru.otus.domain.Answer;
import ru.otus.domain.Exam;
import ru.otus.domain.ExamItem;
import ru.otus.loader.CsvDataFileLoader;

import java.io.IOException;
import java.util.ArrayList;

public class ExamDaoImpl implements ExamDao {

    private static final int NO_RIGHT_ANSWER_INDEX = -1;

    private final String title;
    private final String minPercentageOfCorrectAnswersLabel;
    private final int minPercentageOfCorrectAnswers;

    private final CsvDataFileLoader csvDataFileLoader;
    private final String rightAnswerToken;

    public ExamDaoImpl(String title,
                       String minPercentageOfCorrectAnswersLabel,
                       int minPercentageOfCorrectAnswers,
                       CsvDataFileLoader csvDataFileLoader,
                       String rightAnswerToken) {
        this.title = (title == null) ? "" : title;
        this.minPercentageOfCorrectAnswersLabel = minPercentageOfCorrectAnswersLabel;
        this.minPercentageOfCorrectAnswers = minPercentageOfCorrectAnswers;
        this.csvDataFileLoader = csvDataFileLoader;
        this.rightAnswerToken = rightAnswerToken;
    }

    @Override
    public Exam read() throws IOException, CsvException {

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

                var containsRightAnswerToken = columnValue.contains(rightAnswerToken);
                var answerText = containsRightAnswerToken
                        ? columnValue.replace(rightAnswerToken, "").trim()
                        : columnValue;

                if (StringUtils.isBlank(answerText)) {
                    continue;
                }

                answers.add(new Answer(answerText));

                if (containsRightAnswerToken) {
                    rightAnswerIndex = answers.size() - 1;
                }
            }

            if (answers.isEmpty() || StringUtils.isBlank(question) || rightAnswerIndex == NO_RIGHT_ANSWER_INDEX) {
                continue;
            }

            examItems.add(new ExamItem(question, answers, rightAnswerIndex));
        }

        if (examItems.isEmpty()) {
            return null;
        }

        return new Exam(title, minPercentageOfCorrectAnswersLabel, minPercentageOfCorrectAnswers, examItems);
    }
}
