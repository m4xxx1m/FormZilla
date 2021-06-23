package ru.raptors.team.formzilla.models;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.UUID;

import ru.raptors.team.formzilla.enums.FormStatus;
import ru.raptors.team.formzilla.interfaces.Action;
import ru.raptors.team.formzilla.interfaces.Saveable;

public class Form implements Saveable {
    private String ID;
    private FormStatus status;
    public String title;
    public ArrayList<Question> questions;
    public ArrayList<User> staff;
    public ArrayList<UserAnswer> userAnswers;

    public Form()
    {
        ID = Helper.generateID();
    }

    public Form(String ID) {
        this.ID = ID;
    }

    public Form(String ID, FormStatus status, String title) {
        this.ID = ID;
        this.status = status;
        this.title = title;
    }

    public Form(String title, ArrayList<Question> questions) {
        this();
        this.title = title;
        this.questions = questions;
    }

    public Form(DataSnapshot dataForm) {
        //Todo: Конструктор из DataSnapshot
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

    public void save(Context context)
    {

    }

    public void loadFromPhone(Context context)
    {

    }

    public FormStatus getStatus() {
        return status;
    }

    public void setStatus(FormStatus status) {
        this.status = status;
    }

    public String getID() {
        return ID;
    }
}
