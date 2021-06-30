package ru.raptors.team.formzilla.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.QuestionTypeEnum;

public class TextQuestion extends Question {
    public String answer;

    public TextQuestion() {
        super();
        questionType = QuestionTypeEnum.TextAnswer;
    }

    public TextQuestion(String answer) {
        this();
        this.answer = answer;
    }

    public TextQuestion(DataSnapshot dataSnapshot) {
        super();
        if(dataSnapshot.hasChild("Question")) question = dataSnapshot.child("Question").getValue(String.class);
        if(dataSnapshot.hasChild("Type")) questionType = new QuestionType(dataSnapshot.child("Type").getValue(String.class)).questionTypeEnum;
        if(dataSnapshot.hasChild("UserAnswers")) {
            String userAnswer = dataSnapshot.child("UserAnswers").getChildren().iterator().next().getValue(String.class);
            answer = userAnswer;
        }
    }
}
