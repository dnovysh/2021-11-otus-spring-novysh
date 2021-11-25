package ru.otus.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Exam {

    @Getter
    private final String examTitle;

    private final List<ExamItem> examItems;

    public Exam(String examTitle, List<ExamItem> examItems) {
        this.examTitle = examTitle;
        this.examItems = new ArrayList<>(examItems);
    }

    public List<ExamItem> getExamItems() {
        return new ArrayList<>(examItems);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(examTitle);
        sb.append("\n");
        examItems.forEach(sb.append("\n")::append);
        return sb.toString();
    }
}
