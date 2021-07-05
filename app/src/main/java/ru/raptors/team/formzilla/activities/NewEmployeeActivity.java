package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.models.User;

public class NewEmployeeActivity extends AppCompatActivity {

    TextInputEditText lastName;
    TextInputEditText firstName;
    TextInputEditText position;
    TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);
        findAndSetViews();
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // писать ниже

    }

    private void findAndSetViews()
    {
        lastName = findViewById(R.id.input_last_name);
        firstName = findViewById(R.id.input_first_name);
        position = findViewById(R.id.input_position);
        email = findViewById(R.id.email);
    }

    public void createEmployee(View view) {
        if(!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty())
        {
            User nowUser = User.getNowUser(this);
            User employee = nowUser.generateUser(firstName.getText().toString(), lastName.getText().toString());
            employee.setCompany(nowUser.getCompany());
            employee.setCompanyID(nowUser.getCompanyID());
            employee.save(getApplicationContext());
            nowUser.addEmployeeToStaff(employee);
            nowUser.save(getApplicationContext());
            Intent goToMainActivityIntent = new Intent(NewEmployeeActivity.this, MainActivity.class);
            startActivity(goToMainActivityIntent);
        }
    }
}