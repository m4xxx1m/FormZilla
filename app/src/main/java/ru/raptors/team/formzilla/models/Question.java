package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.OnAnsweredListenerEnum;
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

    //  Todo: сохраняет вопрос в SQLite
    public void save(Context context)
    {

    }

    //  Todo: загружает вопрос из SQLite
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
}
