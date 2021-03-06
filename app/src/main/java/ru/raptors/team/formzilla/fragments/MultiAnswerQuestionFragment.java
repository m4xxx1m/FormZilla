package ru.raptors.team.formzilla.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.activities.AnswerQuestionActivity;
import ru.raptors.team.formzilla.activities.MainActivity;
import ru.raptors.team.formzilla.databases.NowUserDatabase;
import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.MultiAnswersQuestion;
import ru.raptors.team.formzilla.models.SingleAnswerQuestion;
import ru.raptors.team.formzilla.models.User;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MultiAnswersQuestion question = (MultiAnswersQuestion) formToPass.questions.get(questionNumber);
        TextView questionTextView = view.findViewById(R.id.text_question);
        questionTextView.setText(question.question);
        LinearLayout checkboxPlace = view.findViewById(R.id.place_holder_for_check_boxes);
        for(String answer : question.answers)
        {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(answer);
            checkboxPlace.addView(checkBox);
        }
        AnswerQuestionActivity answerQuestionActivity = (AnswerQuestionActivity) getActivity();
        MaterialButton materialButton = getActivity().findViewById(R.id.to_next_question);
        if (questionNumber + 1 < formToPass.questions.size()) {
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0; i < checkboxPlace.getChildCount(); i++)
                    {
                        CheckBox checkBox = (CheckBox) checkboxPlace.getChildAt(i);
                        if(checkBox.isChecked())
                        {
                            question.selectedAnswers.add(checkBox.getText().toString());
                        }
                    }
                    if(question.selectedAnswers.size() > 0) {
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
                        // ?????????? ???????????????????????????? - ???????????????? ???????? ???? ???????? ??????????
                    }
                }
            });
        }
        else{
            materialButton.setText(R.string.done);
            materialButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0; i < checkboxPlace.getChildCount(); i++)
                    {
                        CheckBox checkBox = (CheckBox) checkboxPlace.getChildAt(i);
                        if(checkBox.isChecked())
                        {
                            question.selectedAnswers.add(checkBox.getText().toString());
                        }
                    }
                    if(question.selectedAnswers.size() > 0) {
                        formToPass.setStatus(FormStatusEnum.Passed);
                        formToPass.save(getContext());
                        User nowUser = User.getNowUser(getContext());
                        nowUser.passForm(formToPass);
                        nowUser.uploadFormToFirebase(formToPass);
                        Intent goToMainMenuIntent = new Intent(getActivity(),
                                MainActivity.class);
                        startActivity(goToMainMenuIntent);
                    }
                    else {
                        // ?????????? ???????????????????????????? - ???????????????? ???????? ???? ???????? ??????????
                    }
                }
            });
        }
    }
}