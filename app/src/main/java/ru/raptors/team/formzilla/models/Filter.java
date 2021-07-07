package ru.raptors.team.formzilla.models;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.raptors.team.formzilla.databases.FiltersDatabase;
import ru.raptors.team.formzilla.interfaces.Action;

public class Filter {
    public String ID;
    public String filter;
    public String category;
    public List<User> staff;

    public Filter()
    {
        ID = Helper.generateID();
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

    public Filter(DataSnapshot dataFilter, String category) {
        this();
        this.ID = dataFilter.getKey();
        if(dataFilter.hasChild("Filter")) this.filter = dataFilter.child("Filter").getValue(String.class);
        this.category = category;
        if(dataFilter.hasChild("Staff")) {
            String staffPack = dataFilter.child("Staff").getValue(String.class);
            for (String employeeID : staffPack.split(" ")) {
                staff.add(new User(employeeID));
            }
        }

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
        if(filtersDatabase.hasFilter(ID)) filtersDatabase.update(this);
        else filtersDatabase.insert(this);
    }

    public void loadFromPhone(Context context)
    {
        FiltersDatabase filtersDatabase;
        filtersDatabase = new FiltersDatabase(context);
        Filter filter = filtersDatabase.select(ID);
        if(filter != null) {
            this.filter = filter.filter;
            this.category = filter.category;
            this.staff = filter.staff;
        }
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
