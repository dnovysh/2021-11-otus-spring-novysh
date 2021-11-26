package ru.otus.util;

import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import ru.otus.domain.Answer;
import ru.otus.domain.ExamItem;
import ru.otus.domain.ExamItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class ExamItemBuilderImpl implements ExamItemBuilder {

    public static final int ANSWER_INDEX_ERROR = -1;

    private List<Answer> answers = null;
    @Getter
    private String question = "";
    @Getter
    private int rightAnswerIndex = ANSWER_INDEX_ERROR;

    @Override
    public ExamItemBuilder addAnswer(Answer answer) throws IllegalArgumentException {
        if (!tryAddAnswer(answer)) {
            throw new IllegalArgumentException(
                    "The specified answer mustn't be null, empty, or consists only of white-space characters");
        }

        return this;
    }

    @Override
    public boolean tryAddAnswer(Answer answer) {
        if (answer == null || StringUtils.isBlank(answer.getText())) {
            return false;
        }

        if (answers == null) {
            answers = new ArrayList<>();
        }

        answers.add(answer);

        return true;
    }

    @Override
    public ExamItemBuilder setQuestion(String question) {
        this.question = question;
        return this;
    }

    @Override
    public ExamItemBuilder setRightAnswerIndex(int rightAnswerIndex) {
        this.rightAnswerIndex = rightAnswerIndex;
        return this;
    }

    @Override
    public ExamItem build() {
        if (answers == null || answers.isEmpty() ||
                StringUtils.isBlank(question) ||
                rightAnswerIndex < 0 ||
                rightAnswerIndex >= answers.size()) {
            return null;
        }

        return new ExamItem(question, answers, rightAnswerIndex);
    }

    @Override
    public int getLastAnswerIndex() {
        if (answers == null || answers.isEmpty()) {
            return ANSWER_INDEX_ERROR;
        }

        return answers.size() - 1;
    }

    @Override
    public ExamItemBuilder createNewExamItemBuilder() {
        return new ExamItemBuilderImpl();
    }
}
