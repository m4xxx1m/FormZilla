package ru.raptors.team.formzilla.models;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import ru.raptors.team.formzilla.databases.FormsDatabase;
import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.enums.QuestionTypeEnum;
import ru.raptors.team.formzilla.interfaces.Action;

public class Form implements Serializable {
    private String ID;
    private FormStatusEnum status;
    public String title;
    public ArrayList<Question> questions;
    public ArrayList<User> staff;
    private ArrayList<UserAnswer> userAnswers;

    public Form()
    {
        ID = Helper.generateID();
        questions = new ArrayList<Question>();
        staff = new ArrayList<User>();
        userAnswers = new ArrayList<UserAnswer>();
    }

    public Form(String ID) {
        this();
        this.ID = ID;
    }

    public Form(String ID, FormStatusEnum status, String title) {
        this();
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
        if(dataSnapshot.hasChild("Title")) title = dataSnapshot.child("Title").getValue(String.class);
        if(dataSnapshot.hasChild("Status")) setStatus(new FormStatus(dataSnapshot.child("Status").getValue(String.class)).getFormStatus());
        if(status == FormStatusEnum.Created)
        {
            if(dataSnapshot.hasChild("Staff")) unpackStaff(dataSnapshot.child("Staff").getValue(String.class));
        }
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

    // Здесь форма отправляется всем сотрудникам. которые указаны в staff
    public void sendToStaff()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        for(User employee : staff) {
            DatabaseReference formReference = firebaseDatabase.getReference("Accounts").child(employee.getID()).child("Forms").child(this.ID);
            DatabaseReference databaseReference = formReference.child("Title");
            databaseReference.setValue(title);
            databaseReference = formReference.child("Status");
            FormStatus status = new FormStatus(FormStatusEnum.Available);
            databaseReference.setValue(status.toString());
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                DatabaseReference questionReference = formReference.child("Questions").child(question.getID());
                databaseReference = questionReference.child("Question");
                databaseReference.setValue(question.question);
                databaseReference = questionReference.child("Type");
                QuestionType questionType = new QuestionType(question.questionType);
                databaseReference.setValue(questionType.toString());
                if (question.questionType != QuestionTypeEnum.TextAnswer) {
                    MultipleQuestion multipleQuestion = (MultipleQuestion) question;
                    for (int j = 0; j < multipleQuestion.answers.size(); j++) {
                        databaseReference = questionReference.child("Answers").child(Integer.toString(j));
                        databaseReference.setValue(multipleQuestion.answers.get(j));
                    }
                }
            }
        }
    }

    //  Todo: здесь должны перебираться все сотрудники в Firebase.
    // потом метод получает сотрудников, у которых есть форма с такой же ID и статусом passed
    public void getStaffAnswers(Context context) {
        for (User employee : staff) {
            DatabaseReference employeeReference = FirebaseDatabase.getInstance().getReference("Accounts").child(employee.getID());
            DatabaseReference formReference = employeeReference.child("Forms").child(ID);
            formReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataForm) {
                    if (dataForm.exists()) {
                        if (dataForm.hasChild("Status")) {
                            FormStatusEnum status = new FormStatus(dataForm.child("Status").getValue(String.class)).formStatusEnum;
                            if (status == FormStatusEnum.Passed) {
                                Form passedForm = new Form(dataForm);
                                for (Question passedQuestion : passedForm.questions) {
                                    for (Question thisFormQuestion : Form.this.questions) {
                                        if (passedQuestion.ID.equals(thisFormQuestion.getID())) {
                                            switch (passedQuestion.questionType) {
                                                case TextAnswer: {
                                                    TextQuestion textQuestion = (TextQuestion) passedQuestion;
                                                    Form.this.userAnswers.add(new UserAnswer(textQuestion.answer, employee));
                                                    thisFormQuestion.callListeners(textQuestion.answer, employee.getID(), context);
                                                    break;
                                                }
                                                case SingleAnswer: {
                                                    SingleAnswerQuestion singleAnswerQuestion = (SingleAnswerQuestion) passedQuestion;
                                                    Form.this.userAnswers.add(new UserAnswer(singleAnswerQuestion.selectedAnswer, employee));
                                                    thisFormQuestion.callListeners(singleAnswerQuestion.selectedAnswer, employee.getID(), context);
                                                    break;
                                                }
                                                case MultiAnswer: {
                                                    MultiAnswersQuestion multiAnswersQuestion = (MultiAnswersQuestion) passedQuestion;
                                                    for (String employeeAnswer : multiAnswersQuestion.selectedAnswers) {
                                                        Form.this.userAnswers.add(new UserAnswer(employeeAnswer, employee));
                                                        thisFormQuestion.callListeners(employeeAnswer, employee.getID(), context);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Form.this.save(context);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void save(Context context)
    {
        FormsDatabase formsDatabase;
        formsDatabase = new FormsDatabase(context);
        if(formsDatabase.hasForm(ID)) formsDatabase.update(this);
        else formsDatabase.insert(this);
        for(Question question : questions)
        {
            question.save(context);
        }
    }

    public void loadFromPhone(Context context)
    {
        FormsDatabase formsDatabase;
        formsDatabase = new FormsDatabase(context);
        Form form = formsDatabase.select(ID);
        if(form != null) {
            this.status = form.status;
            this.title = form.title;
            this.questions = form.questions;
            this.staff = form.staff;
        }
    }

    public String packQuestions()
    {
        String result = "";
        for(Question question : questions)
        {
            result += question.getID() + " ";
        }
        return result;
    }

    public void unpackQuestions(String pack, Context context)
    {
        String[] unpackedQuestionsIDs = pack.split(" ");
        for(String unpackedQuestionID : unpackedQuestionsIDs)
        {
            questions.add(Question.loadFromPhone(unpackedQuestionID, context));
        }
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

    @Override
    public String toString() {
        return "Form{" +
                "ID='" + ID + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                '}';
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
