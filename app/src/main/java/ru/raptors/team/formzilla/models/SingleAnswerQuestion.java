package ru.raptors.team.formzilla.models;

import java.util.ArrayList;

public class SingleAnswerQuestion extends MultipleQuestion {
    public String selectedAnswer;

    public SingleAnswerQuestion(ArrayList<String> answers) {
        super(answers);
    }
}
