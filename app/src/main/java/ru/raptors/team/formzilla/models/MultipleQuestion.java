package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.raptors.team.formzilla.enums.QuestionTypeEnum;

public abstract class MultipleQuestion extends Question{
    public List<String> answers;

    protected MultipleQuestion() {
        super();
        questionType = QuestionTypeEnum.MultiAnswer;
        this.answers = new ArrayList<String>();
    }

    protected MultipleQuestion(String ID) {
        super(ID);
        this.answers = new ArrayList<String>();
    }

    public MultipleQuestion(ArrayList<String> answers) {
        super();
        this.answers = answers;
    }

    public String packAnswers()
    {
        String result = "";
        for(String answer : answers)
        {
            result += answer + "%regex%";
        }
        return result;
    }

    public void unpackAnswers(String pack)
    {
        String[] unpackedAnswers = pack.split("%regex%");
        answers = Arrays.asList(unpackedAnswers);
    }
}
