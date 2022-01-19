package ru.otus.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExamItem {

    private final String question;
    private final ArrayList<Answer> answers;
    private final int rightAnswerPosition;

    public ExamItem(String question, List<Answer> answers, int rightAnswerIndex) {
        this.question = question;
        this.answers = new ArrayList<>(answers);
        this.rightAnswerPosition = convertAnswerIndexToPosition(rightAnswerIndex);
    }

    public boolean answerIsRight(int answerPosition) {
        return answerPosition == rightAnswerPosition;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(question);
        sb.append("\n");
        IntStream.range(0, answers.size()).forEach(index -> sb.append("\n")
                .append(convertAnswerIndexToPosition(index))
                .append(" ").append(answers.get(index)));
        sb.append("\n");
        return sb.toString();
    }

    private int convertAnswerIndexToPosition(int answerIndex) {
        return answerIndex + 1;
    }
}
