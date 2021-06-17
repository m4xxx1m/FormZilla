package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.FormStatus;
import ru.raptors.team.formzilla.interfaces.Action;

public class Form {
    private String ID;
    private FormStatus status;
    public ArrayList<Question> questions;
    public ArrayList<User> staff;
    public ArrayList<UserAnswer> userAnswers;

    public void sendToStaff()
    {

    }

    public void getStaffAnswersAndDoAction(Context context, Action action)
    {

    }
}
