package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.fragments.AvailableFormsFragment;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.fragments.MultiAnswerQuestionFragment;
import ru.raptors.team.formzilla.fragments.PassedFormsFragment;
import ru.raptors.team.formzilla.fragments.SingleAnswerQuestionFragment;
import ru.raptors.team.formzilla.fragments.TextAnswerQuestionFragment;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.MultiAnswersQuestion;

public class AnswerQuestionActivity extends AppCompatActivity {

    public final static String FORMTOPASS = "formToPass";

    private RelativeLayout placeHolder;
    private Toolbar toolbar;

    private Form formToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        getFormFromPreviousActivity();
        findAndSetViews();
        toolbar.setTitle(formToPass.title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // писать ниже
        switch (formToPass.questions.get(0).questionType)
        {
            case TextAnswer:
                openTextQuestionFragment(0);
                break;

            case SingleAnswer:
                openSingleAnswerQuestionFragment(0);
                break;

            case MultiAnswer:
                openMultiAnswerQuestionFragment(0);
                break;
        }
        findViewById(R.id.to_next_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // next button
            }
        });
    }

    public void openTextQuestionFragment(int questionNum)
    {
        placeHolder.removeAllViews();
        TextAnswerQuestionFragment textAnswerQuestionFragment = TextAnswerQuestionFragment.newInstance(formToPass, questionNum);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder_for_fragment, textAnswerQuestionFragment)
                .commit();
    }

    public void openSingleAnswerQuestionFragment(int questionNum)
    {
        placeHolder.removeAllViews();
        SingleAnswerQuestionFragment singleAnswerQuestionFragment = SingleAnswerQuestionFragment.newInstance(formToPass, questionNum);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder_for_fragment, singleAnswerQuestionFragment)
                .commit();
    }

    public void openMultiAnswerQuestionFragment(int questionNum)
    {
        placeHolder.removeAllViews();
        MultiAnswerQuestionFragment multiAnswerQuestionFragment = MultiAnswerQuestionFragment.newInstance(formToPass, questionNum);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder_for_fragment, multiAnswerQuestionFragment)
                .commit();
    }

    private void findAndSetViews()
    {
        toolbar = findViewById(R.id.toolbar);
        placeHolder = findViewById(R.id.place_holder_for_fragment);
    }

    private void getFormFromPreviousActivity()
    {
        formToPass = (Form) getIntent().getSerializableExtra(FORMTOPASS);
    }
}