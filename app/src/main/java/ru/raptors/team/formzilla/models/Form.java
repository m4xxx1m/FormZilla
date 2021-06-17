package ru.raptors.team.formzilla.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

import ru.raptors.team.formzilla.enums.FormStatus;
import ru.raptors.team.formzilla.interfaces.Action;

public class Form {
    private String ID;
    private FormStatus status;
    public ArrayList<Question> questions;
    public ArrayList<User> staff;
    public ArrayList<UserAnswer> userAnswers;

    public Form()
    {
        generateID();
    }

    public Form(ArrayList<Question> questions) {
        this();
        this.questions = questions;
    }

    public void sendToStaff()
    {
        // здесь форма должна отправляться всем сотрудникам. которые указаны в staff
    }

    public void getStaffAnswersAndDoAction(Context context, Action action)
    {
        // здесь должны перебираться все сотрудники в Firebase, в том чиле и их наследники.
        // потом метод получает сотрудников, у которых есть форма с такой же ID и статусом passed
    }

    private void generateID()
    {
        UUID uuid = UUID.randomUUID();
        ID = uuid.toString();
    }
}
