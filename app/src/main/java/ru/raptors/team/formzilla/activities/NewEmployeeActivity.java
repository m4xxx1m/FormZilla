package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import ru.raptors.team.formzilla.R;

public class NewEmployeeActivity extends AppCompatActivity {

    TextInputEditText lastName;
    TextInputEditText firstName;
    TextInputEditText position;
    TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);
        lastName = findViewById(R.id.input_last_name);
        firstName = findViewById(R.id.input_first_name);
        position = findViewById(R.id.input_position);
        email = findViewById(R.id.email);

        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}