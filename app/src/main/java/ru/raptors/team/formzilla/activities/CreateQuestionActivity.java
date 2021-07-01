package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.MultiAnswersQuestion;
import ru.raptors.team.formzilla.models.MultipleQuestion;
import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.SingleAnswerQuestion;
import ru.raptors.team.formzilla.models.TextQuestion;
import ru.raptors.team.formzilla.models.User;

public class CreateQuestionActivity extends AppCompatActivity {

    private Form form;
    private MaterialToolbar toolbar;
    private EditText questionNameText;
    private SwitchMaterial freeAnswerSwitch;
    private SwitchMaterial multipleAnswerSwitch;
    private SwitchMaterial filterCreationListenerSwitch;
    private LinearLayout answersLayout;
    private LinearLayout addAnswerLayout;
    private LinearLayout answersPlaceHolder;
    private LinearLayout placeHolder;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        findAndSetViews();
        getFormFromPreviousActivity();
        // писать ниже
        question = new SingleAnswerQuestion();
        //setSupportActionBar(toolbar);
        toolbar.setTitle(form.title);
        freeAnswerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    question = new TextQuestion();
                    answersPlaceHolder.removeView(answersLayout);
                    answersPlaceHolder.removeView(addAnswerLayout);
                    placeHolder.removeView(multipleAnswerSwitch);
                }
                else {
                    question = new SingleAnswerQuestion();
                    answersPlaceHolder.addView(answersLayout);
                    answersPlaceHolder.addView(addAnswerLayout);
                    placeHolder.addView(multipleAnswerSwitch);
                }
            }
        });
        multipleAnswerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) question = new MultiAnswersQuestion();
                else question = new SingleAnswerQuestion();
            }
        });
        filterCreationListenerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
        addAnswer(null);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Костя, это кнопка предыдущий вопрос
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.next_button) {
                    // TODO: Костя, это кнопка следующий вопрос
                    return true;
                }
                return false;
            }
        });
    }

    private void findAndSetViews()
    {
        toolbar = findViewById(R.id.toolbar);
        questionNameText = findViewById(R.id.enter_question_name);
        freeAnswerSwitch = findViewById(R.id.free_answer);
        multipleAnswerSwitch = findViewById(R.id.multiple_answer);
        filterCreationListenerSwitch = findViewById(R.id.create_filter_by_answer);
        answersLayout = findViewById(R.id.answers_layout);
        addAnswerLayout = findViewById(R.id.add_answer_layout);
        answersPlaceHolder = findViewById(R.id.answers_place_holder);
        placeHolder = findViewById(R.id.multiple_answer_place_holder);
    }

    private void getFormFromPreviousActivity()
    {
        form = (Form) getIntent().getSerializableExtra(CreatedFormsFragment.FORM);
    }

    public void addAnswer(View view)
    {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View answerView = layoutInflater.inflate(R.layout.answer_layout, answersLayout, false);
        answersLayout.addView(answerView);
        answerView.findViewById(R.id.delete_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answersLayout.removeView(answerView);
            }
        });
    }

    public void addQuestion(View view)
    {
        boolean questionAddResult = addThisQuestionToForm();
        if(questionAddResult) {
            goToQuestionCreationActivity();
        }
    }

    private boolean addThisQuestionToForm()
    {
        boolean result = true;
        switch (question.questionType)
        {
            case TextAnswer:
            {
                this.question = (TextQuestion) question;
                break;
            }
            default:
            {
                for(int i = 0; i < answersLayout.getChildCount(); i++)
                {
                    View answer = answersLayout.getChildAt(i);
                    EditText answerEditText = answer.findViewById(R.id.enter_question_answer);
                    if(answerEditText.getText() != null && !answerEditText.getText().toString().isEmpty())
                    {
                        ((MultipleQuestion) question).answers.add(answerEditText.getText().toString());
                    }
                    else {
                        // нет ответа
                        // вывод предупреждения
                        result = false;
                    }
                }
                break;
            }
        }
        if(!questionNameText.getText().toString().isEmpty()) question.question = questionNameText.getText().toString();
        else
        {
            // нет ответа
            // вывод предупреждения
            result = false;
        }
        if(result) form.questions.add(question);
        return result;
    }

    private void goToQuestionCreationActivity()
    {
        Intent createFormIntent = new Intent(CreateQuestionActivity.this, CreateQuestionActivity.class);
        createFormIntent.putExtra(CreatedFormsFragment.FORM, form);
        startActivity(createFormIntent);
    }


    public void complete(View view)
    {
        boolean questionAddResult = addThisQuestionToForm();
        if(questionAddResult) {
            User nowUser = User.getNowUser(this);
            nowUser.addForm(form);
            form.sendToStaff();
            Intent createFormIntent = new Intent(CreateQuestionActivity.this, MainActivity.class);
            startActivity(createFormIntent);
        }
    }
}