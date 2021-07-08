package ru.raptors.team.formzilla.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.navigation.NavigationView;
import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.databases.NowUserDatabase;
import ru.raptors.team.formzilla.fragments.AvailableFormsFragment;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.fragments.PassedFormsFragment;
import ru.raptors.team.formzilla.fragments.StaffFragment;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.User;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout placeHolder;
    private RecyclerView availableFormsRecyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        placeHolder = findViewById(R.id.place_holder);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBar supportActionBar = getSupportActionBar();
        VectorDrawableCompat indicator
                = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
        indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
        supportActionBar.setHomeAsUpIndicator(indicator);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = findViewById(R.id.drawer);

        NowUserDatabase nowUserDatabase = new NowUserDatabase(this);
        User nowUser = nowUserDatabase.select();
        nowUser.loadUserFromFirebaseAndDoAction(getApplicationContext(), MainActivity.this, null);
        nowUser.loadStaffFromFirebaseAndDoAction(this, null);
        nowUser.loadFiltersFromFirebase(this, MainActivity.this, null);
        for (Form form : nowUser.getCreatedForms()) {
            form.getStaffAnswers(getApplicationContext());
        }

        openAvailableFormsFragment();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.availableForms:{
                                openAvailableFormsFragment();
                                break;
                            }
                            case R.id.passedForms:{
                                openPassedFormsFragment();
                                break;
                            }
                            case R.id.createdForms:{
                                openCreatedFormsFragment();
                                break;
                            }
                            case R.id.staff:{
                                openStaffFragment();
                                break;
                            }
                            case R.id.create_form:{
                                openCreateFormActivity();
                                item.setChecked(false);
                                item.setCheckable(false);
                                break;
                            }
                        }
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().deleteDatabase("users.db");
                getApplicationContext().deleteDatabase("nowUser.db");
                getApplicationContext().deleteDatabase("forms.db");
                getApplicationContext().deleteDatabase("questions.db");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openCreateFormActivity() {
        Form form = new Form();
        Intent createFormIntent = new Intent(MainActivity.this ,
                EnterFormNameActivity.class);
        createFormIntent.putExtra(CreatedFormsFragment.FORM, form);
        startActivity(createFormIntent);
    }

    private void openAvailableFormsFragment()
    {
        toolbar.setTitle(R.string.available_forms);
        placeHolder.removeAllViews();
        AvailableFormsFragment availableFormsFragment = AvailableFormsFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, availableFormsFragment)
                .commit();
    }

    private void openPassedFormsFragment()
    {
        toolbar.setTitle(R.string.passed_forms);
        placeHolder.removeAllViews();
        PassedFormsFragment passedFormsFragment = PassedFormsFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, passedFormsFragment)
                .commit();
    }


    private void openCreatedFormsFragment()
    {
        toolbar.setTitle(R.string.created_forms);
        placeHolder.removeAllViews();
        CreatedFormsFragment createdFormsFragment = CreatedFormsFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, createdFormsFragment)
                .commit();
    }

    private void openStaffFragment()
    {
        toolbar.setTitle(R.string.staff);
        placeHolder.removeAllViews();
        StaffFragment staffFragment = StaffFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, staffFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
