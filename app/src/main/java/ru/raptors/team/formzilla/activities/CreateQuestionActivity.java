package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.enums.FormStatusEnum;
import ru.raptors.team.formzilla.enums.QuestionTypeEnum;
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
    private EditText filterCategoryText;
    private TextView errorText;
    private SwitchMaterial freeAnswerSwitch;
    private SwitchMaterial multipleAnswerSwitch;
    private SwitchMaterial filterCreationListenerSwitch;
    private LinearLayout answersLayout;
    private LinearLayout addAnswerLayout;
    private LinearLayout answersPlaceHolder;
    private LinearLayout placeHolder;

    private boolean createFilter = false;

    private Question question;
    private Question questionFromPreviousActivity;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        findAndSetViews();
        getFormFromPreviousActivity();
        question = new SingleAnswerQuestion();
        if(getIntent().hasExtra(CreatedFormsFragment.QUESTION)) {
            questionFromPreviousActivity = (Question) getIntent().getSerializableExtra(CreatedFormsFragment.QUESTION);
        }
        // ???????????? ????????
        question = new SingleAnswerQuestion();
        //setSupportActionBar(toolbar);
        toolbar.setTitle(form.title);
        freeAnswerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    CreateQuestionActivity.this.question = new TextQuestion();
                    answersPlaceHolder.removeView(answersLayout);
                    answersPlaceHolder.removeView(addAnswerLayout);
                    placeHolder.removeView(multipleAnswerSwitch);
                }
                else {
                    CreateQuestionActivity.this.question = new SingleAnswerQuestion();
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
                if (isChecked) {
                    createFilter = true;
                    filterCategoryText.setVisibility(View.VISIBLE);
                } else {
                    createFilter = false;
                    filterCategoryText.setVisibility(View.INVISIBLE);
                }
            }
        });
        addAnswer(null);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ???????????? ???????????????????? ????????????
                CreateQuestionActivity.this.finish();

            }
        });
        if(questionFromPreviousActivity == null || !containsQuestion(form.questions, questionFromPreviousActivity)) {
            toolbar.getMenu().findItem(R.id.next_button).setVisible(false);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.next_button) {
                    // ???????????? ?????????????????? ????????????
                    Intent createFormIntent = new Intent(CreateQuestionActivity.this, CreateQuestionActivity.class);
                    createFormIntent.putExtra(CreatedFormsFragment.FORM, form);
                    //nextQuestion = form.questions.get(form.questions.indexOf(question) + 1);
                    //createFormIntent.putExtra(CreatedFormsFragment.QUESTION, question);
                    startActivity(createFormIntent);
                    return true;
                }
                return false;
            }
        });
    }

    private boolean containsQuestion(ArrayList<Question> questions, Question question)
    {
        boolean result = false;
        for(Question formQuestion : questions)
        {
            if (formQuestion.getID().equals(question.getID())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void findAndSetViews()
    {
        toolbar = findViewById(R.id.toolbar);
        questionNameText = findViewById(R.id.enter_question_name);
        filterCategoryText = findViewById(R.id.filter_category);
        errorText = findViewById(R.id.error);
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
                question = (TextQuestion) question;
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
                        // ?????? ????????????
                        // ?????????? ????????????????????????????
                        result = false;
                    }
                }
                break;
            }
        }
        if(!questionNameText.getText().toString().isEmpty()) question.question = questionNameText.getText().toString();
        else
        {
            // ?????? ????????????
            // ?????????? ????????????????????????????
            result = false;
        }
        if(!filterCategoryText.getText().toString().trim().isEmpty() && result)
        {
            question.addOnAnsweredListenerCreateFilter(filterCategoryText.getText().toString(), User.getNowUser(getApplicationContext()));
        }
        else if(createFilter) result = false;
        if (result) form.questions.add(question);
        else errorText.setVisibility(View.VISIBLE);

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
            form.setStatus(FormStatusEnum.Created);
            nowUser.addForm(form);
            nowUser.uploadFormToFirebase(form);
            form.sendToStaff();
            nowUser.save(this);
            Intent createFormIntent = new Intent(CreateQuestionActivity.this, MainActivity.class);
            startActivity(createFormIntent);
        }
    }
}