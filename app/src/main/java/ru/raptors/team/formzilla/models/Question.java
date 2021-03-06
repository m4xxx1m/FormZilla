package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

import ru.raptors.team.formzilla.databases.QuestionsDatabase;
import ru.raptors.team.formzilla.enums.OnAnsweredListenerEnum;
import ru.raptors.team.formzilla.enums.QuestionTypeEnum;
import ru.raptors.team.formzilla.interfaces.OnAnsweredListener;

public class Question implements Serializable {
    protected String ID;
    public String question;
    public QuestionTypeEnum questionType;
    public ArrayList<OnAnsweredListener> onAnsweredListeners;

    public Question() {
        ID = Helper.generateID();
        this.onAnsweredListeners = new ArrayList<OnAnsweredListener>();
    }

    public Question(String ID) {
        this();
        this.ID = ID;
    }

    public void addOnAnsweredListenerCreateFilter(String filterCategory, User user)
    {
        CreateFilterOnAnsweredListener createFilterOnAnsweredListener = new CreateFilterOnAnsweredListener();
        createFilterOnAnsweredListener.category = filterCategory;
        createFilterOnAnsweredListener.user = user;
        onAnsweredListeners.add(createFilterOnAnsweredListener);
    }

    public void callListeners(String answer, String userID, Context context)
    {
        for(OnAnsweredListener onAnsweredListener : onAnsweredListeners)
        {
            onAnsweredListener.onAnswered(answer, userID, context);
        }
    }

    public void save(Context context)
    {
        QuestionsDatabase questionsDatabase;
        questionsDatabase = new QuestionsDatabase(context);
        if(questionsDatabase.hasQuestion(ID)) questionsDatabase.update(this);
        else questionsDatabase.insert(this);
        questionsDatabase.close();
    }

    public static Question loadFromPhone(String ID, Context context)
    {
        QuestionsDatabase questionsDatabase;
        questionsDatabase = new QuestionsDatabase(context);
        return questionsDatabase.select(ID);
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
        if(pack == null || pack.isEmpty()) return;
        String[] unpackedListeners = pack.split("%%regex");
        for(int i = 0 ; i < unpackedListeners.length; i++)
        {
            int onAnsweredListenerInt;
            try{
                onAnsweredListenerInt = Integer.parseInt(unpackedListeners[i]);
                if(onAnsweredListenerInt == 0)
                {
                    i++;
                    String category = unpackedListeners[i];
                    i++;
                    String userID = unpackedListeners[i];
                    addOnAnsweredListenerCreateFilter(category, new User(userID));
                }
            }
            catch (NumberFormatException numberFormatException)
            {
                numberFormatException.printStackTrace();
            }
        }
    }

    private void addOnAnsweredListener(OnAnsweredListener onAnsweredListener)
    {
        this.onAnsweredListeners.add(onAnsweredListener);
    }

    public String getID() {
        return ID;
    }
}
