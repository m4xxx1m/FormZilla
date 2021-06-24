package ru.raptors.team.formzilla.models;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.enums.QuestionTypeEnum;
import ru.raptors.team.formzilla.interfaces.Action;
import ru.raptors.team.formzilla.interfaces.Saveable;

public class Form implements Saveable {
    private String ID;
    private FormStatusEnum status;
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

    public Form(String ID, FormStatusEnum status, String title) {
        this.ID = ID;
        this.status = status;
        this.title = title;
    }

    public Form(String title, ArrayList<Question> questions) {
        this();
        this.title = title;
        this.questions = questions;
    }

    public Form(DataSnapshot dataSnapshot) {
        this();
        ID = dataSnapshot.getKey();
        if(dataSnapshot.hasChild("Status")) setStatus(new FormStatus(dataSnapshot.child("Status").getValue(String.class)).getFormStatus());
        if(dataSnapshot.hasChild("Questions"))
        {
            DataSnapshot dataQuestions = dataSnapshot.child("Questions");
            for(DataSnapshot question : dataQuestions.getChildren())
            {
                QuestionTypeEnum questionType = new QuestionType(question.child("Type").getValue(String.class)).questionTypeEnum;
                switch (questionType)
                {
                    case SingleAnswer:
                    {
                        questions.add(new SingleAnswerQuestion(question));
                        break;
                    }
                    case MultiAnswer:
                    {
                        questions.add(new MultiAnswersQuestion(question));
                        break;
                    }
                    case TextAnswer:
                    {
                        questions.add(new TextQuestion(question));
                        break;
                    }
                }
            }
        }
    }

    // здесь форма должна отправляться всем сотрудникам. которые указаны в staff
    public void sendToStaff()
    {

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

    public FormStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FormStatusEnum status) {
        this.status = status;
    }

    public String getID() {
        return ID;
    }
}
