package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import ru.raptors.team.formzilla.enums.FormStatus;
import ru.raptors.team.formzilla.enums.Gender;
import ru.raptors.team.formzilla.interfaces.Action;
import ru.raptors.team.formzilla.interfaces.Saveable;

public class User implements Saveable {
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

    public static User getNowUser()
    {
        User result = null;

        return result;
    }

    public User generateUser(String firstName, String lastName)
    {
        User result = null;
        ID = Helper.generateID();
        result.firstName = firstName;
        result.lastName = lastName;
        return result;
    }

    public void getFormsFromFirebaseAndDoAction(Context context, Action action)
    {
        // получает все доступные и пройденные опросы с Firebase
    }

    public void uploadFormsToFirebaseAndDoAction(Context context, Action action)
    {
        // загружает все пройденные формы с результатами на Firebase
    }

    public void getFiltersFromFirebaseAndDoAction(Context context, Action action)
    {
        // получает все доступные фильтры с Firebase
    }

    public void uploadFiltersOnFirebase(Context context)
    {

    }

    public void save(Context context)
    {

    }

    public void loadFromPhone(Context context)
    {

    }

    public boolean hasFilter(Filter filter)
    {
        boolean result = false;
        for(Filter userFilter : filters)
        {
            if(userFilter.category.equals(filter.category)
            && userFilter.filter.equals(filter.filter))
            {
                result = true;
            }
        }
        return result;
    }

    public void addFilter(Filter filter)
    {
        if(!hasFilter(filter)) filters.add(filter);
        else {
            //Todo: что делать, если такой фильтр уже есть?
        }
    }

    public ArrayList<Form> getAvailableForms() {
        ArrayList<Form> result = new ArrayList<Form>();
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatus.Available) result.add(form);
        }
        return result;
    }

    public ArrayList<Form> getPassedForms() {
        ArrayList<Form> result = new ArrayList<Form>();
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatus.Passed) result.add(form);
        }
        return result;
    }

    public ArrayList<Form> getCreatedForms() {
        ArrayList<Form> result = new ArrayList<Form>();
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatus.Created) result.add(form);
        }
        return result;
    }

    public String getID() {
        return ID;
    }

    public ArrayList<Form> getForms() {
        return forms;
    }
}
