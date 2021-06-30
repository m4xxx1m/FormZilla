package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

import ru.raptors.team.formzilla.databases.FormsDatabase;
import ru.raptors.team.formzilla.databases.QuestionsDatabase;
import ru.raptors.team.formzilla.enums.OnAnsweredListenerEnum;
import ru.raptors.team.formzilla.enums.QuestionTypeEnum;
import ru.raptors.team.formzilla.interfaces.OnAnsweredListener;
import ru.raptors.team.formzilla.interfaces.Saveable;

public class Question implements Saveable, Serializable {
    private String ID;
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

    public void callListeners(String answer, String userID)
    {
        for(OnAnsweredListener onAnsweredListener : onAnsweredListeners)
        {
            onAnsweredListener.onAnswered(answer, userID);
        }
    }

    public void save(Context context)
    {
        QuestionsDatabase questionsDatabase;
        questionsDatabase = new QuestionsDatabase(context);
        questionsDatabase.update(this);
    }

    public void loadFromPhone(Context context)
    {
        QuestionsDatabase questionsDatabase;
        questionsDatabase = new QuestionsDatabase(context);
        Question question = questionsDatabase.select(ID);
        this.question = question.question;
        this.questionType = question.questionType;
        this.onAnsweredListeners = question.onAnsweredListeners;
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
        String[] unpackedListeners = pack.split(" ");
        for(int i = 0 ; i < unpackedListeners.length; i++)
        {
            int onAnsweredListenerInt;
            try{
                onAnsweredListenerInt = Integer.parseInt(unpackedListeners[i]);
                if(OnAnsweredListenerEnum.fromInteger(onAnsweredListenerInt).equals(OnAnsweredListenerEnum.CreateFilter))
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
