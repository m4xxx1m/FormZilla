package ru.raptors.team.formzilla.models;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.QuestionTypeEnum;

public abstract class MultipleQuestion extends Question{
    public ArrayList<String> answers;

    protected MultipleQuestion() {
        super();
        questionType = QuestionTypeEnum.MultiAnswer;
        this.answers = new ArrayList<String>();
    }

    public MultipleQuestion(ArrayList<String> answers) {
        super();
        this.answers = answers;
    }
}
