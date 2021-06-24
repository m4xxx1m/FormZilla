package ru.raptors.team.formzilla.models;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.enums.GenderEnum;
import ru.raptors.team.formzilla.interfaces.Action;
import ru.raptors.team.formzilla.interfaces.Saveable;

public class User implements Saveable {
    private String ID;
    private String login;
    private String password;
    private GenderEnum gender;
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

    // получает все доступные и пройденные опросы с Firebase
    public void getFormsFromFirebaseAndDoAction(Context context, Action action)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Forms");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataForm : snapshot.getChildren())
                    {
                        Form form = new Form(dataForm);
                        Form existForm = findFormByID(form.getID());
                        if(existForm == null)
                        {
                            forms.add(form);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // загружает все пройденные формы с результатами на Firebase
    public void uploadPassedFormsToFirebaseAndDoAction(Context context, Action action)
    {
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatusEnum.Passed) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference formReference = firebaseDatabase.getReference("Accounts").child(ID).child("Forms").child(form.getID());
                DatabaseReference databaseReference = formReference.child("Status");
                FormStatus status = new FormStatus(form.getStatus());
                databaseReference.setValue(status.toString());
                for(int i = 0; i < form.questions.size(); i++)
                {
                    Question question = form.questions.get(i);
                    DatabaseReference questionReference = formReference.child("Questions").child(Integer.toString(i));
                    databaseReference = questionReference.child("Question");
                    databaseReference.setValue(question.question);
                    databaseReference = questionReference.child("Type");
                    QuestionType questionType = new QuestionType(question.questionType);
                    databaseReference.setValue(questionType.toString());
                    switch (question.questionType)
                    {
                        case SingleAnswer:
                        {
                            SingleAnswerQuestion singleAnswerQuestion = (SingleAnswerQuestion) question;
                            databaseReference = questionReference.child("UserAnswers").child("Answer");
                            databaseReference.setValue(singleAnswerQuestion.selectedAnswer);
                            break;
                        }
                        case MultiAnswer:
                        {
                            MultiAnswersQuestion multiAnswersQuestion = (MultiAnswersQuestion) question;
                            for(int j = 0 ; j < multiAnswersQuestion.selectedAnswers.size() ; j++)
                            {
                                databaseReference = questionReference.child("UserAnswers").child(Integer.toString(j));
                                databaseReference.setValue(multiAnswersQuestion.selectedAnswers.get(j));
                            }
                            break;
                        }
                        case TextAnswer:
                        {
                            TextQuestion textQuestion = (TextQuestion) question;
                            databaseReference = questionReference.child("UserAnswers").child("Answer");
                            databaseReference.setValue(textQuestion.answer);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void getFiltersFromFirebaseAndDoAction(Context context, Action action)
    {
        // Todo: получает все доступные фильтры с Firebase
    }

    public void uploadFiltersOnFirebase(Context context)
    {
        // Todo: загружает все фильтры на Firebase
    }

    public void save(Context context)
    {
        // Todo: сохраняет данные пользователя на смартфоне в SQLite
    }

    public void saveInFirebase(Context context)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference accountReference = firebaseDatabase.getReference("Accounts").child(ID);
        DatabaseReference databaseReference;
        databaseReference = accountReference.child("Login");
        databaseReference.setValue(login);
        databaseReference = accountReference.child("Password");
        databaseReference.setValue(password);
        databaseReference = accountReference.child("FirstName");
        databaseReference.setValue(firstName);
        databaseReference = accountReference.child("LastName");
        databaseReference.setValue(lastName);
        databaseReference = accountReference.child("Company");
        databaseReference.setValue(company);
        databaseReference = accountReference.child("CompanyID");
        databaseReference.setValue(companyID);
        databaseReference = accountReference.child("Gender");
        databaseReference.setValue(new Gender(gender).getGender());
    }

    public void loadFromPhone(Context context)
    {
        // Todo: загружает информацию о пользователе с SQLite
    }

    private Form findFormByID(String ID)
    {
        for (Form form : forms)
        {
            if(form.getID().equals(ID)) return form;
        }
        return null;
    }

    public boolean hasFilter(Filter filter)
    {
        boolean result = false;
        for(Filter userFilter : filters)
        {
            if(userFilter.ID.equals(filter.ID)
                    && userFilter.category.equals(filter.category)
                    && userFilter.filter.equals(filter.filter))
            {
                result = true;
            }
        }
        return result;
    }

    public Filter findFilterByID(String ID)
    {
        Filter result = null;
        for(Filter userFilter : filters)
        {
            if(userFilter.ID.equals(ID))
            {
                result = userFilter;
                break;
            }
        }
        return result;
    }


    public void addFilter(Filter filter)
    {
        if(!hasFilter(filter)) filters.add(filter);
        else {
            Filter existFilter = findFilterByID(filter.ID);
            for(User staffUser : filter.staff)
            {
                if(!existFilter.hasUserInStaff(staffUser))
                {
                    existFilter.staff.add(staffUser);
                }
            }
        }
    }

    public ArrayList<Form> getAvailableForms() {
        ArrayList<Form> result = new ArrayList<Form>();
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatusEnum.Available) result.add(form);
        }
        return result;
    }

    public ArrayList<Form> getPassedForms() {
        ArrayList<Form> result = new ArrayList<Form>();
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatusEnum.Passed) result.add(form);
        }
        return result;
    }

    public ArrayList<Form> getCreatedForms() {
        ArrayList<Form> result = new ArrayList<Form>();
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatusEnum.Created) result.add(form);
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
