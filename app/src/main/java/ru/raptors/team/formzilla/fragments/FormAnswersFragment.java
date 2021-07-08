package ru.raptors.team.formzilla.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.UserAnswer;

public class FormAnswersFragment extends Fragment {

    private static final String ARG_FORM = "formUserAnswers";

    private Form form;

    public FormAnswersFragment() {
    }

    public static FormAnswersFragment newInstance(Form form) {
        FormAnswersFragment fragment = new FormAnswersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORM, form);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            form = (Form) getArguments().getSerializable(ARG_FORM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_answers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout placeHolder = view.findViewById(R.id.question_place_holder);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(Question question : form.questions) {
            View questionView = layoutInflater.inflate(R.layout.question_answers_item, placeHolder, false);
            ((TextView) questionView.findViewById(R.id.question)).setText(question.question);
            LinearLayout answersPlaceHolder = questionView.findViewById(R.id.answers_place_holder);
            Map<String,Integer> questionAndAnswersCount = getCountOfAnswersForQuestion(question.getID());
            placeHolder.addView(questionView);
            for(String answer : questionAndAnswersCount.keySet())
            {
                View answerView = layoutInflater.inflate(R.layout.answer_item, answersPlaceHolder, false);
                ((TextView) answerView.findViewById(R.id.answer)).setText(answer);
                ((TextView) answerView.findViewById(R.id.procent)).setText(Integer.toString(questionAndAnswersCount.get(answer)));
                answersPlaceHolder.addView(answerView);
            }
        }
    }

    private Map<String,Integer> getCountOfAnswersForQuestion(String questionID)
    {
        Map<String,Integer> result = new HashMap<String, Integer>();
        for(UserAnswer userAnswer : form.getUserAnswers())
        {
            if(userAnswer.questionID.equals(questionID))
            {
                if(result.containsKey(userAnswer.answer)) result.put(userAnswer.answer, result.get(userAnswer.answer) + 1);
                else result.put(userAnswer.answer, 1);
            }
        }
        return result;
    }
}