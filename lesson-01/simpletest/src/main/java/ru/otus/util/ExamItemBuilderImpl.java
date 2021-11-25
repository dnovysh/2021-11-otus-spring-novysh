package ru.otus.util;

import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import ru.otus.domain.Answer;
import ru.otus.domain.ExamItem;
import ru.otus.domain.ExamItemBuilder;

import java.util.ArrayList;

public class ExamItemBuilderImpl implements ExamItemBuilder {

    private ArrayList<Answer> answers = null;
    @Getter
    private String question = "";
    @Getter
    private int rightAnswerIndex = -1;

    @Override
    public ExamItemBuilder addAnswer(Answer answer) {
        if (answer != null && !StringUtils.isBlank(answer.getText())) {
            if (answers == null){
                answers = new ArrayList<>();
            }

            answers.add(answer);
        }

        return this;
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
        if ( answers == null || answers.size() == 0 ||
                StringUtils.isBlank(question) ||
                rightAnswerIndex < 0 ||
                rightAnswerIndex >= answers.size()) {
            return null;
        }

        return new ExamItem(question, answers, rightAnswerIndex);
    }

    @Override
    public ExamItemBuilder createNewExamItemBuilder() {
        return new ExamItemBuilderImpl();
    }
}
