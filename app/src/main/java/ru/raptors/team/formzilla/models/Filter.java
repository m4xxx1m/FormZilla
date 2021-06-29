package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ru.raptors.team.formzilla.databases.FiltersDatabase;
import ru.raptors.team.formzilla.databases.QuestionsDatabase;
import ru.raptors.team.formzilla.enums.OnAnsweredListenerEnum;
import ru.raptors.team.formzilla.interfaces.Action;
import ru.raptors.team.formzilla.interfaces.OnAnsweredListener;
import ru.raptors.team.formzilla.interfaces.Saveable;

public class Filter implements Saveable {
    public String ID;
    public String filter;
    public String category;
    public ArrayList<User> staff;

    public Filter()
    {
        staff = new ArrayList<User>();
    }

    public Filter(String ID) {
        this();
        this.ID = ID;
    }

    public Filter(String filter, String category) {
        this();
        this.filter = filter;
        this.category = category;
    }

    public Filter(String filter, String category, ArrayList<User> staff) {
        this();
        this.filter = filter;
        this.category = category;
        this.staff = staff;
    }

    public void getStaffAndDoAction(Context context, Action action)
    {
        // Todo: метод получает всех пользователей, которые подходят под этот фильтр, с Firebase
        // (!он не должен перебирать пользователей)
        // (в firebase создаётся string название фильтра и все пользователи, которые под него подходят)
    }

    public boolean hasUserInStaff(User user)
    {
        boolean result = false;
        for(User userInStaff : staff)
        {
            if(user.getID().equals(userInStaff.getID()))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public void save(Context context)
    {
        FiltersDatabase filtersDatabase;
        filtersDatabase = new FiltersDatabase(context);
        filtersDatabase.update(this);
    }

    public void loadFromPhone(Context context)
    {
        FiltersDatabase filtersDatabase;
        filtersDatabase = new FiltersDatabase(context);
        Filter filter = filtersDatabase.select(ID);
        this.filter = filter.filter;
        this.category = filter.category;
        this.staff = filter.staff;
    }

    public String packStaff()
    {
        String result = "";
        for(User user : staff)
        {
            result += user.getID() + " ";
        }
        return result;
    }

    public void unpackStaff(String pack)
    {
        String[] unpackedStaffID = pack.split(" ");
        for(String unpackedUserID : unpackedStaffID)
        {
            staff.add(new User(unpackedUserID));
        }
    }
}
