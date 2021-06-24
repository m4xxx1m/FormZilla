package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.QuestionTypeEnum;
import ru.raptors.team.formzilla.interfaces.OnAnsweredListener;
import ru.raptors.team.formzilla.interfaces.Saveable;

public class Question implements Saveable {
    public String question;
    public QuestionTypeEnum questionType;
    public ArrayList<OnAnsweredListener> onAnsweredListeners;

    public Question() {
        this.onAnsweredListeners = new ArrayList<OnAnsweredListener>();
    }

    public void addOnAnsweredListenerCreateFilter(String filterCategory, User user)
    {

    }

    public void callListeners(String answer, String userID)
    {
        for(OnAnsweredListener onAnsweredListener : onAnsweredListeners)
        {
            onAnsweredListener.onAnswered(answer, userID);
        }
    }

    public void save(Context context)
    {

    }

    public void loadFromPhone(Context context)
    {

    }

    public String packOnAnsweredListeners()
    {
        String result = "";
        for(OnAnsweredListener onAnsweredListener : onAnsweredListeners)
        {
            result += onAnsweredListener.toString();
        }
        return result;
    }

    public void unpackOnAnsweredListeners(String pack)
    {

    }

    private void addOnAnsweredListener(OnAnsweredListener onAnsweredListener)
    {
        this.onAnsweredListeners.add(onAnsweredListener);
    }
}
