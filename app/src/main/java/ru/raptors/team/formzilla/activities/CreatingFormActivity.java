package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.models.Form;

public class CreatingFormActivity extends AppCompatActivity {

    private final String[] options = new String[] { "Без повтора", "Каждый день", "Раз в неделю",
            "Раз в месяц", "Раз в год" };
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item, options);
        autoCompleteTextView = findViewById(R.id.actv_drop_down);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setText(options[0], false);
        getFormFromPreviousActivity();
        toolbar.setTitle(form.title);
    }

    private void getFormFromPreviousActivity() {
        form = (Form) getIntent().getSerializableExtra(CreatedFormsFragment.FORM);
    }

    public void goToFormCreationActivity(View v)
    {
        Intent createQuestionsIntent = new Intent(CreatingFormActivity.this,
                CreateQuestionActivity.class);
        createQuestionsIntent.putExtra(CreatedFormsFragment.FORM, form);
        startActivity(createQuestionsIntent);
    }
}