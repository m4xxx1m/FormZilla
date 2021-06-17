package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.Gender;
import ru.raptors.team.formzilla.interfaces.Action;

public class User {
    private String ID;
    private String login;
    private String password;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String company;
    private String companyID;
    private ArrayList<Form> forms;
    private ArrayList<User> staff;
    private ArrayList<Filter> filters;

    public User(String ID) {
        this.ID = ID;
    }

    public User(String ID, String firstName, String lastName, String company, String companyID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.companyID = companyID;
    }

    public User generateUser(String firstName, String lastName)
    {
        User result = null;

        return result;
    }

    public void getFormsFromFirebaseAndDoAction(Context context, Action action)
    {

    }

    public void uploadFormsToFirebaseAndDoAction(Context context, Action action)
    {

    }

    public void getFiltersFromFirebaseAndDoAction(Context context, Action action)
    {

    }
}
