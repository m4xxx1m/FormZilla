package ru.raptors.team.formzilla.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.SingleAnswerQuestion;

public class SingleAnswerQuestionFragment extends Fragment {

    private static final String ARG_FORM = "formToPass";
    private static final String ARG_QUESTION_NUMBER = "questionNumber";

    private Form formToPass;
    private int questionNumber;

    public SingleAnswerQuestionFragment() {
    }

    public static SingleAnswerQuestionFragment newInstance(Form formToPass, int questionNum) {
        SingleAnswerQuestionFragment fragment = new SingleAnswerQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORM, formToPass);
        args.putSerializable(ARG_QUESTION_NUMBER, questionNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            formToPass = (Form)  getArguments().getSerializable(ARG_FORM);
            questionNumber = (Integer)  getArguments().getSerializable(ARG_QUESTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_answer_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioGroup answersRadioGroup = view.findViewById(R.id.answer_radio_group);
        SingleAnswerQuestion question = (SingleAnswerQuestion) formToPass.questions.get(questionNumber);
        TextView questionTextView = view.findViewById(R.id.text_question);
        questionTextView.setText(question.question);
        for(String answer : question.answers)
        {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(answer);
            answersRadioGroup.addView(radioButton);
        }
    }
}