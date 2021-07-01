package ru.raptors.team.formzilla.activities;

import android.os.Bundle;
import android.view.MenuItem;

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
import ru.raptors.team.formzilla.fragments.AvailableFormsFragment;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.fragments.StaffFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout placeHolder;
    RecyclerView availableFormsRecyclerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        placeHolder = findViewById(R.id.place_holder);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBar supportActionBar = getSupportActionBar();
        VectorDrawableCompat indicator
                = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
        indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
        supportActionBar.setHomeAsUpIndicator(indicator);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = findViewById(R.id.drawer);

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
                        }
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void openAvailableFormsFragment()
    {
        placeHolder.removeAllViews();
        AvailableFormsFragment availableFormsFragment = AvailableFormsFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.place_holder, availableFormsFragment)
                .commit();
    }

    private void openCreatedFormsFragment()
    {
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
