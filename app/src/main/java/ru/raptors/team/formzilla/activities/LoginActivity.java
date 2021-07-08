package ru.raptors.team.formzilla.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.databases.NowUserDatabase;
import ru.raptors.team.formzilla.interfaces.Action;
import ru.raptors.team.formzilla.models.User;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginEditText;
    TextInputEditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findAndSetViews();
        // писать ниже


        //getApplicationContext().deleteDatabase("nowUser.db");

/*        getApplicationContext().deleteDatabase("users.db");
        getApplicationContext().deleteDatabase("nowUser.db");
        getApplicationContext().deleteDatabase("forms.db");
        getApplicationContext().deleteDatabase("questions.db");*/

        Context context = getApplicationContext();
        NowUserDatabase nowUserDatabase = new NowUserDatabase(context);
        User nowUser = new NowUserDatabase(context).select();
        if(nowUser != null)
        {
            goToMainActivity();
        }
        else {
            findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = new User();
                    user.loadUserFromFirebaseByLoginAndPasswordAndDoAction(loginEditText.getText().toString(), passwordEditText.getText().toString(), new Action() {
                        @Override
                        public void run() {
                            user.save(context);
                            nowUserDatabase.insert(loginEditText.getText().toString(), passwordEditText.getText().toString(), user.getID());
                            goToMainActivity();
                        }
                    }, context, LoginActivity.this);
                }
            });
            findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void goToMainActivity()
    {
        Intent toMainActivity = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(toMainActivity);
        finish();
    }

    private void findAndSetViews()
    {
        loginEditText = findViewById(R.id.login_enter_text);
        passwordEditText = findViewById(R.id.password_edit_text);
    }
}