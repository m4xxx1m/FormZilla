package ru.raptors.team.formzilla.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.material.button.MaterialButton;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.activities.AnswerQuestionActivity;
import ru.raptors.team.formzilla.activities.EnterFormNameActivity;
import ru.raptors.team.formzilla.activities.MainActivity;
import ru.raptors.team.formzilla.databases.NowUserDatabase;
import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.SingleAnswerQuestion;
import ru.raptors.team.formzilla.models.User;

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
        for (String answer : question.answers) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(answer);
            answersRadioGroup.addView(radioButton);
        }
        AnswerQuestionActivity answerQuestionActivity = (AnswerQuestionActivity) getActivity();
        MaterialButton materialButton = getActivity().findViewById(R.id.to_next_question);
        if (questionNumber + 1 < formToPass.questions.size()) {
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answersRadioGroup.getCheckedRadioButtonId() != -1) {
                        RadioButton checkedRadioButton = view.findViewById(answersRadioGroup.getCheckedRadioButtonId());
                        question.selectedAnswer = checkedRadioButton.getText().toString();
                        switch (formToPass.questions.get(questionNumber + 1).questionType) {
                            case TextAnswer:
                                answerQuestionActivity.openTextQuestionFragment(questionNumber + 1);
                                break;

                            case SingleAnswer:
                                answerQuestionActivity.openSingleAnswerQuestionFragment(questionNumber + 1);
                                break;

                            case MultiAnswer:
                                answerQuestionActivity.openMultiAnswerQuestionFragment(questionNumber + 1);
                                break;
                        }
                    }
                    else {
                        // предупреждение - выберете ответ
                    }
                }
            });
        }
        else{
            materialButton.setText(R.string.done);
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answersRadioGroup.getCheckedRadioButtonId() != 0) {
                        RadioButton checkedRadioButton = view.findViewById(answersRadioGroup.getCheckedRadioButtonId());
                        question.selectedAnswer = checkedRadioButton.getText().toString();
                        formToPass.setStatus(FormStatusEnum.Passed);
                        formToPass.save(getContext());
                        User nowUser = User.getNowUser(getContext());
                        nowUser.passForm(formToPass);
                        nowUser.uploadFormToFirebase(formToPass);
                        Intent goToMainMenuIntent = new Intent(getActivity(),
                                MainActivity.class);
                        startActivity(goToMainMenuIntent);
                        getActivity().finish();
                    }
                    else {
                        // предупреждение - выберете ответ
                    }
                }
            });
        }
    }
}