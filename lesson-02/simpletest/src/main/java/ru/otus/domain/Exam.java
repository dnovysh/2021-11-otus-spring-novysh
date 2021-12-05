package ru.otus.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Exam implements Iterable<ExamItem> {

    @Getter
    private final String examTitle;
    @Getter
    private final String minPercentageOfCorrectAnswersLabel;
    @Getter
    private final int minPercentageOfCorrectAnswers;
    private final List<ExamItem> examItems;

    public Exam(String examTitle,
                String minPercentageOfCorrectAnswersLabel,
                int minPercentageOfCorrectAnswers,
                List<ExamItem> examItems) {
        this.examTitle = examTitle;
        this.minPercentageOfCorrectAnswersLabel = minPercentageOfCorrectAnswersLabel;
        this.minPercentageOfCorrectAnswers = minPercentageOfCorrectAnswers;
        this.examItems = new ArrayList<>(examItems);
    }

    public List<ExamItem> getExamItems() {
        return new ArrayList<>(examItems);
    }

    public ExamItem getExamItem(int index) {
        return examItems.get(index);
    }

    public String getTitleBlock() {
        return String.format("%s\n\n%s : %s%%\n\n",
                examTitle, minPercentageOfCorrectAnswersLabel, minPercentageOfCorrectAnswers);
    }

    @Override
    public String toString() {
        return examItems.stream().map(ExamItem::toString)
                .collect(Collectors.joining("\n", getTitleBlock() , ""));
    }

    @Override
    public Iterator<ExamItem> iterator() {
        return new Iterator<>() {

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
