package ru.otus.util;

import org.apache.commons.lang3.StringUtils;
import ru.otus.domain.Exam;
import ru.otus.domain.ExamBuilder;
import ru.otus.domain.ExamItem;

import java.util.ArrayList;
import java.util.List;

public class ExamBuilderImpl implements ExamBuilder {

    private String title = "";

    private List<ExamItem> examItems = null;

    @Override
    public ExamBuilder setTitle(String title) {
        this.title = title;

        return this;
    }

    @Override
    public ExamBuilder addExamItem(ExamItem examItem) throws IllegalArgumentException {
        if (!tryAddExamItem(examItem)) {
            throw new IllegalArgumentException("The specified ExamItem mustn't be null");
        }

        return this;
    }

    @Override
    public boolean tryAddExamItem(ExamItem examItem) {
        if (examItem == null) {
            return false;
        }

        if (examItems == null) {
            examItems = new ArrayList<>();
        }

        examItems.add(examItem);

        return true;
    }

    @Override
    public Exam build() {
        if (examItems == null || examItems.isEmpty() || StringUtils.isBlank(title)){
            return null;
        }

        return new Exam(title, examItems);
    }

    @Override
    public ExamBuilder createNewExamBuilder() {
        return new ExamBuilderImpl();
    }
}
