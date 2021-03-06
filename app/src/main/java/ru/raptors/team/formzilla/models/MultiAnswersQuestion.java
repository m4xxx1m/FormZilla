package ru.raptors.team.formzilla.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class MultiAnswersQuestion extends MultipleQuestion{
    public ArrayList<String> selectedAnswers;

    public MultiAnswersQuestion() {
        super();
        selectedAnswers = new ArrayList<String>();
    }

    public MultiAnswersQuestion(String ID) {
        super(ID);
        selectedAnswers = new ArrayList<String>();
    }

    public MultiAnswersQuestion(ArrayList<String> answers) {
        super(answers);
        selectedAnswers = new ArrayList<String>();
    }

    public MultiAnswersQuestion(DataSnapshot dataSnapshot) {
        super(dataSnapshot.getKey());
        selectedAnswers = new ArrayList<String>();
        ID = dataSnapshot.getKey();
        if(dataSnapshot.hasChild("OnAnsweredListeners")) unpackOnAnsweredListeners(dataSnapshot.child("OnAnsweredListeners").getValue(String.class));
        if(dataSnapshot.hasChild("Question")) question = dataSnapshot.child("Question").getValue(String.class);
        if(dataSnapshot.hasChild("Type")) questionType = new QuestionType(dataSnapshot.child("Type").getValue(String.class)).questionTypeEnum;
        ArrayList<String> answers = new ArrayList<String>();
        for(DataSnapshot answer : dataSnapshot.child("Answers").getChildren())
        {
            answers.add(answer.getValue(String.class));
        }
        this.answers = answers;
        if(dataSnapshot.hasChild("UserAnswers")) {
            for(DataSnapshot answer : dataSnapshot.child("UserAnswers").getChildren())
            {
                selectedAnswers.add(answer.getValue(String.class));
            }
        }
    }
}
