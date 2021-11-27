package ru.otus.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Exam implements Iterable<ExamItem> {

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

    public ExamItem getExamItem(int index) {
        return examItems.get(index);
    }

    @Override
    public String toString() {
        return  examItems.stream().map(ExamItem::toString)
                .collect(Collectors.joining("\n", examTitle + "\n\n", ""));
    }

    @Override
    public Iterator<ExamItem> iterator() {
        return new Iterator<ExamItem>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < examItems.size();
            }

            @Override
            public ExamItem next() {
                return examItems.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
