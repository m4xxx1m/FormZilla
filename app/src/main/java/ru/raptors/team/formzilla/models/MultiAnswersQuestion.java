package ru.raptors.team.formzilla.models;

import java.util.ArrayList;

public class MultiAnswersQuestion extends MultipleQuestion{
    public ArrayList<String> selectedAnswers;

    public MultiAnswersQuestion(ArrayList<String> answers) {
        super(answers);
    }
}
