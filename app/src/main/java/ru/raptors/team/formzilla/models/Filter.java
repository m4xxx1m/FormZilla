package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.util.ArrayList;

import ru.raptors.team.formzilla.interfaces.Action;

public class Filter {
    public String filter;
    public ArrayList<User> staff;

    public Filter(String filter) {
        this.filter = filter;
    }

    public Filter(String filter, ArrayList<User> staff) {
        this.filter = filter;
        this.staff = staff;
    }

    public void getStaffAndDoAction(Context context, Action action)
    {

    }
}
