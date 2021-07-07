package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.fragments.CreatedFormsFragment;
import ru.raptors.team.formzilla.models.Filter;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.User;

public class CreatingFormActivity extends AppCompatActivity {

    public final static String CHOSEN_FILTER = "filter";

    private final String[] options = new String[] { "Без повтора", "Каждый день", "Раз в неделю",
            "Раз в месяц", "Раз в год" };
    private LinearLayout filtersPlaceHolder;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private Form form;
    private ArrayList<Filter> filters;
    private final int REQUEST_CODE = 1;

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
        filters = new ArrayList<Filter>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item, options);
        autoCompleteTextView = findViewById(R.id.actv_drop_down);
        filtersPlaceHolder = findViewById(R.id.filters_place_holder);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setText(options[0], false);
        getFormFromPreviousActivity();
        toolbar.setTitle(form.title);
        findViewById(R.id.add_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChoosingFiltersActivity = new Intent(CreatingFormActivity.this,
                        ChoosingFiltersActivity.class);
                startActivityForResult(toChoosingFiltersActivity, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Filter filter = (Filter) data.getSerializableExtra(CHOSEN_FILTER);
            filters.add(filter);
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View filterView = layoutInflater.inflate(R.layout.filter_item, filtersPlaceHolder, false);
            TextView filterText = filterView.findViewById(R.id.filter_text);
            filterText.setText(filter.category + ": " + filter.filter);
            ImageView removeFilterButton = filterView.findViewById(R.id.remove_filter);
            removeFilterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filtersPlaceHolder.removeView(filterView);
                    filters.remove(filter);
                }
            });
            filtersPlaceHolder.addView(filterView);
        }
    }

    private void getFormFromPreviousActivity() {
        form = (Form) getIntent().getSerializableExtra(CreatedFormsFragment.FORM);
    }

    public void addFilter(Filter filter) {

    }

    private void formStaff()
    {
        ArrayList<User> staff = User.getNowUser(getApplicationContext()).getStaff();
        ArrayList<User> formStaff = (ArrayList<User>) staff.clone();
        for(Filter filter : filters)
        {
            for (User employee : staff) {
                if (!filter.hasUserInStaff(employee))
                {
                    formStaff.remove(employee);
                }
            }
        }
        form.staff = formStaff;
    }

    public void goToFormCreationActivity(View v)
    {
        formStaff();
        Intent createQuestionsIntent = new Intent(CreatingFormActivity.this,
                CreateQuestionActivity.class);
        createQuestionsIntent.putExtra(CreatedFormsFragment.FORM, form);
        startActivity(createQuestionsIntent);
    }
}