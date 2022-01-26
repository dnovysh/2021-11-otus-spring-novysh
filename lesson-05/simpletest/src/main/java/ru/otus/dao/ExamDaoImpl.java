package ru.otus.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.otus.domain.Answer;
import ru.otus.domain.Exam;
import ru.otus.domain.ExamItem;
import ru.otus.loader.CsvDataFileLoader;
import ru.otus.loader.ResourceFileAsStringLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExamDaoImpl implements ExamDao {

    private static final int NO_RIGHT_ANSWER_INDEX = -1;

    private final ResourceFileAsStringLoader resourceFileAsStringLoader;
    private final CsvDataFileLoader csvDataFileLoader;

    public ExamDaoImpl(ResourceFileAsStringLoader resourceFileAsStringLoader,
                       CsvDataFileLoader csvDataFileLoader) {
        this.resourceFileAsStringLoader = resourceFileAsStringLoader;
        this.csvDataFileLoader = csvDataFileLoader;
    }

    @Override
    public Exam read() {

        ExamHeaderDto examHeaderDto = readExamHeader(resourceFileAsStringLoader);

        List<ExamItem> examItems = readExamItems(csvDataFileLoader, examHeaderDto.rightAnswerToken());

        return new Exam(examHeaderDto.title(),
                examHeaderDto.minPercentageOfCorrectAnswersLabel(),
                examHeaderDto.minPercentageOfCorrectAnswers(),
                examItems);
    }

    private ExamHeaderDto readExamHeader(ResourceFileAsStringLoader resourceFileAsStringLoader) {

        final String headerJson = resourceFileAsStringLoader.load();

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(headerJson, ExamHeaderDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Exam header properties are incorrect ", e);
        }
    }

    private List<ExamItem> readExamItems(CsvDataFileLoader csvDataFileLoader, String rightAnswerToken) {

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
                                Arrays.toString(row), rightAnswerToken));
            }

            examItems.add(new ExamItem(question, answers, rightAnswerIndex));
        }

        if (examItems.isEmpty()) {
            throw new RuntimeException("There are no tasks in the test");
        }

        return examItems;
    }
}
