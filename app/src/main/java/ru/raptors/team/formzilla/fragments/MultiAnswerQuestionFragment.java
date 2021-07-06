package ru.raptors.team.formzilla.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.models.Form;

public class MultiAnswerQuestionFragment extends Fragment {

    private static final String ARG_FORM = "formToPass";
    private static final String ARG_QUESTION_NUMBER = "questionNumber";

    private Form formToPass;
    private int questionNumber;

    public MultiAnswerQuestionFragment() {
    }

    public static MultiAnswerQuestionFragment newInstance(Form formToPass, int questionNum) {
        MultiAnswerQuestionFragment fragment = new MultiAnswerQuestionFragment();
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
        return inflater.inflate(R.layout.fragment_multi_answer_question, container, false);
    }
}