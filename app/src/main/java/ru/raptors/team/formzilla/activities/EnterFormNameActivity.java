package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.models.Form;

public class EnterFormNameActivity extends AppCompatActivity {

    private Form form;
    private TextInputLayout formNameInputLayout;
    private TextInputEditText formNameInputText;
    private MaterialButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_form_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getFormFromPreviousActivity();
        findAndSetViews();
    }

    private void findAndSetViews()
    {
        formNameInputText = findViewById(R.id.form_name_input);
        formNameInputLayout = findViewById(R.id.enterFormName);
        nextButton = findViewById(R.id.go_to_create_form_activity_button);
        formNameInputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!formNameInputText.getText().toString().isEmpty())
                    formNameInputLayout.setError(null);
                return false;
            }
        });
    }

    private void getFormFromPreviousActivity()
    {
        form = (Form) getIntent().getSerializableExtra(CreatedFormsFragment.FORM);
    }

    public void goToFormCreationActivity(View view)
    {
        boolean setFormNameResult = setFormName();
        if(setFormNameResult) {
            Intent createFormIntent = new Intent(EnterFormNameActivity.this,
                    CreatingFormActivity.class);
            createFormIntent.putExtra(CreatedFormsFragment.FORM, form);
            startActivity(createFormIntent);
        }
    }

    private boolean setFormName()
    {
        boolean result = false;
        if(!formNameInputText.getText().toString().isEmpty()) {
            form.title = formNameInputText.getText().toString();
            Log.i("FormTitle_EnterFormName", form.title);
            result = true;
            formNameInputLayout.setError(null);
        }
        else {
            formNameInputLayout.setError(getString(R.string.field_cant_be_empty));
        }
        return result;
    }
}