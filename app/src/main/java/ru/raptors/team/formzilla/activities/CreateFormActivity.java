package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.TextQuestion;

public class CreateFormActivity extends AppCompatActivity {

    private Form form;
    private SwitchMaterial freeAnswerSwitch;
    private LinearLayout answersLayout;
    private LinearLayout addAnswerLayout;
    private LinearLayout answersPlaceHolder;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getFormFromPreviousActivity();
        findAndSetViews();
        freeAnswerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    question = new TextQuestion();
                    answersPlaceHolder.removeView(answersLayout);
                    answersPlaceHolder.removeView(addAnswerLayout);
                }
            }
        });
    }

    private void findAndSetViews()
    {
        freeAnswerSwitch = findViewById(R.id.free_answer);
        answersLayout = findViewById(R.id.answers_layout);
        addAnswerLayout = findViewById(R.id.add_answer_layout);
        answersPlaceHolder = findViewById(R.id.answers_place_holder);
    }

    private void getFormFromPreviousActivity()
    {
        form = (Form) getIntent().getSerializableExtra(CreatedFormsFragment.FORM);
    }
}