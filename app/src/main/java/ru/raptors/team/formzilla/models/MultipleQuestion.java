package ru.raptors.team.formzilla.models;

import java.util.ArrayList;

public abstract class MultipleQuestion extends Question{
    public ArrayList<String> answers;

    public MultipleQuestion(ArrayList<String> answers) {
        super();
        this.answers = answers;
    }
}
