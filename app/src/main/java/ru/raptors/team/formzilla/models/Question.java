package ru.raptors.team.formzilla.models;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.QuestionType;
import ru.raptors.team.formzilla.interfaces.OnAnsweredListener;

public abstract class Question {
    String question;
    QuestionType questionType;
    ArrayList<OnAnsweredListener> onAnsweredListeners;

    public Question() {
        this.onAnsweredListeners = new ArrayList<OnAnsweredListener>();
    }

    void setOnAnsweredListener(OnAnsweredListener onAnsweredListener)
    {
        this.onAnsweredListeners.add(onAnsweredListener);
    }

    void onAnswered(String answer)
    {
        for(OnAnsweredListener onAnsweredListener : onAnsweredListeners)
        {
            onAnsweredListener.onAnswered(answer);
        }
    }
}
