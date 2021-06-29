package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.models.Form;

public class EnterFormNameActivity extends AppCompatActivity {

    private Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_form_name);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getFormFromPreviousActivity();
    }

    private void getFormFromPreviousActivity()
    {
        form = (Form) getIntent().getSerializableExtra(CreatedFormsFragment.FORM);
    }
}