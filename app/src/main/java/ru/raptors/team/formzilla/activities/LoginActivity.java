package ru.raptors.team.formzilla.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

/*        getApplicationContext().deleteDatabase("users.db");
        getApplicationContext().deleteDatabase("nowUser.db");
        getApplicationContext().deleteDatabase("forms.db");
        getApplicationContext().deleteDatabase("questions.db");*/

        findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for debugging
                final String admin = "admin";
                if (loginEditText.getText().toString().equals(admin) &&
                        passwordEditText.getText().toString().equals(admin)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                //

                final User user;
                if(User.getNowUser(getApplicationContext()) != null)
                {
                    user = User.getNowUser(getApplicationContext());
                }
                else user = new User();
                user.loadUserFromFirebaseAndDoAction(loginEditText.getText().toString(), passwordEditText.getText().toString(), new Action() {
                    @Override
                    public void run() {
                        user.save(getApplicationContext());
                        NowUserDatabase nowUserDatabase = new NowUserDatabase(getApplicationContext());
                        nowUserDatabase.insert(loginEditText.getText().toString(), passwordEditText.getText().toString(), user.getID());
                        Intent toMainActivity = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(toMainActivity);
                        finish();
                    }
                }, getApplicationContext());
            }
        });
        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findAndSetViews()
    {
        loginEditText = findViewById(R.id.login_enter_text);
        passwordEditText = findViewById(R.id.password_edit_text);
    }

}