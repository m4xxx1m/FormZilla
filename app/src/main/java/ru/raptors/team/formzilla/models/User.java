package ru.raptors.team.formzilla.models;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.raptors.team.formzilla.databases.FormsDatabase;
import ru.raptors.team.formzilla.databases.NowUserDatabase;
import ru.raptors.team.formzilla.databases.UsersDatabase;
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

    public User() {
        ID = Helper.generateID();
        forms = new ArrayList<Form>();
        staff = new ArrayList<User>();
        filters = new ArrayList<Filter>();
    }

    public User(String ID) {
        this();
        this.ID = ID;
    }

    public User(String ID, String firstName, String lastName, String company, String companyID) {
        this(ID);
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.companyID = companyID;
    }

    public static User getNowUser(Context context)
    {
        User result = null;
        NowUserDatabase nowUserDatabase;
        nowUserDatabase = new NowUserDatabase(context);
        result = nowUserDatabase.select();
        return result;
    }

    // метод загружает данные о пользователе по логину и паролю
    public void loadUserFromFirebaseAndDoAction(String enteredLogin, String enteredPassword, Action action, Context context)
    {
        User result = null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataUser: snapshot.getChildren())
                    {
                        String login = "";
                        String password = "";
                        if (dataUser.hasChild("Login")) login = dataUser.child("Login").getValue(String.class);
                        if (dataUser.hasChild("Password")) password = dataUser.child("Password").getValue(String.class);
                        if (login.equals(enteredLogin) && password.equals(enteredPassword)) {
                            if (dataUser.hasChild("FirstName")) firstName = dataUser.child("FirstName").getValue(String.class);
                            if (dataUser.hasChild("LastName")) lastName = dataUser.child("LastName").getValue(String.class);
                            if (dataUser.hasChild("Gender")) gender = new Gender(dataUser.child("Gender").getValue(String.class)).getGender();
                            if (dataUser.hasChild("Company")) company = dataUser.child("Company").getValue(String.class);
                            if (dataUser.hasChild("CompanyID")) companyID = dataUser.child("CompanyID").getValue(String.class);
                            ID = dataUser.getKey();
                            getFormsFromFirebaseAndDoAction(null, context);
                            action.run();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
    public void getFormsFromFirebaseAndDoAction(Action action, Context context)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Forms");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataForm : snapshot.getChildren())
                    {
                        Form form = new Form(dataForm);
                        Log.i("FormTitle", form.title);
                        Log.i("FormStatus", form.getStatus().toString());
                        Form existForm = findFormByID(form.getID());
                        if(existForm == null)
                        {
                            form.save(context);
                        }
                    }
                    save(context);
                    if(action != null) action.run();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // загружает все пройденные формы с результатами на Firebase
    public void uploadPassedFormsToFirebase()
    {
        for(Form form : forms)
        {
            if(form.getStatus() == FormStatusEnum.Passed) {
                uploadFormToFirebase(form);
            }
        }
    }

    public void uploadFormToFirebase(Form form)
    {
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
            if(form.getStatus() == FormStatusEnum.Passed) {
                switch (question.questionType) {
                    case SingleAnswer: {
                        SingleAnswerQuestion singleAnswerQuestion = (SingleAnswerQuestion) question;
                        databaseReference = questionReference.child("UserAnswers").child("Answer");
                        databaseReference.setValue(singleAnswerQuestion.selectedAnswer);
                        break;
                    }
                    case MultiAnswer: {
                        MultiAnswersQuestion multiAnswersQuestion = (MultiAnswersQuestion) question;
                        for (int j = 0; j < multiAnswersQuestion.selectedAnswers.size(); j++) {
                            databaseReference = questionReference.child("UserAnswers").child(Integer.toString(j));
                            databaseReference.setValue(multiAnswersQuestion.selectedAnswers.get(j));
                        }
                        break;
                    }
                    case TextAnswer: {
                        TextQuestion textQuestion = (TextQuestion) question;
                        databaseReference = questionReference.child("UserAnswers").child("Answer");
                        databaseReference.setValue(textQuestion.answer);
                        break;
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

    public void generateLogin()
    {
        //Todo: генерация логина
    }

    public void generatePassword()
    {
        //Todo: генерация пароля
    }

    public void save(Context context)
    {
        UsersDatabase usersDatabase;
        usersDatabase = new UsersDatabase(context);
        if(usersDatabase.hasUser(ID)) usersDatabase.update(this);
        else usersDatabase.insert(this);
        for(Form form : forms)
        {
            form.save(context);
        }
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
        UsersDatabase usersDatabase;
        usersDatabase = new UsersDatabase(context);
        User user = usersDatabase.select(ID);
        this.gender = user.gender;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.company = user.getCompany();
        this.companyID = user.getCompanyID();
        this.forms = user.forms;
        this.staff = user.staff;
        this.filters = user.filters;
    }

    private Form findFormByID(String ID)
    {
        if(forms == null || !forms.isEmpty()) {
            for (Form form : forms) {
                if (form.getID().equals(ID)) return form;
            }
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

    public void addForm(Form form)
    {
        forms.add(form);
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

    public String packForms()
    {
        String result = "";
        for(Form form : forms)
        {
            result += form.getID() + " ";
        }
        return result;
    }

    public void unpackForms(String pack, Context context)
    {
        String[] unpackedFormsIDs = pack.split(" ");
        for(String unpackedFormID : unpackedFormsIDs)
        {
            Form form = new Form(unpackedFormID);
            form.loadFromPhone(context);
            forms.add(form);
        }
    }

    public String packFilters()
    {
        String result = "";
        for(Filter filter : filters)
        {
            result += filter.ID + " ";
        }
        return result;
    }

    public void unpackFilters(String pack, Context context)
    {
        String[] unpackedFiltersIDs = pack.split(" ");
        for(String unpackedFilterID : unpackedFiltersIDs)
        {
            Filter filter = new Filter(unpackedFilterID);
            filter.loadFromPhone(context);
            filters.add(filter);
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

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<User> getStaff() {
        return staff;
    }

    public ArrayList<Filter> getFilters() {
        return filters;
    }
}
