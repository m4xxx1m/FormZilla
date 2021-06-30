package ru.raptors.team.formzilla.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import ru.raptors.team.formzilla.enums.QuestionTypeEnum;

public class SingleAnswerQuestion extends MultipleQuestion {
    public String selectedAnswer;

    public SingleAnswerQuestion()
    {
        questionType = QuestionTypeEnum.SingleAnswer;
    }

    public SingleAnswerQuestion(ArrayList<String> answers) {
        super(answers);
        questionType = QuestionTypeEnum.SingleAnswer;
    }

    public SingleAnswerQuestion(DataSnapshot dataSnapshot) {
        super();
        if(dataSnapshot.hasChild("Question")) question = dataSnapshot.child("Question").getValue(String.class);
        if(dataSnapshot.hasChild("Type")) questionType = new QuestionType(dataSnapshot.child("Type").getValue(String.class)).questionTypeEnum;
        ArrayList<String> answers = new ArrayList<String>();
        for(DataSnapshot answer : dataSnapshot.child("Answers").getChildren())
        {
            answers.add(answer.getValue(String.class));
        }
        this.answers = answers;
        if(dataSnapshot.hasChild("UserAnswers")) {
            String userAnswer = dataSnapshot.child("UserAnswers").getChildren().iterator().next().getValue(String.class);
            selectedAnswer = userAnswer;
        }
    }
}
