package ru.raptors.team.formzilla.models;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.FormStatus;

public class Form {
    private String ID;
    private FormStatus status;
    public ArrayList<Question> questions;
    public ArrayList<User> staff;

    public void sendToStaff()
    {

    }
}
