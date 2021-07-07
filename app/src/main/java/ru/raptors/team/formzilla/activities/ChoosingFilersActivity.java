package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.adapters.FiltersAdapter;

public class ChoosingFilersActivity extends AppCompatActivity {

    private ExpandableListView filtersView;
    private FiltersAdapter adapter;
    public static final String INTENT_NAME = "filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_filers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        filtersView = findViewById(R.id.expListView);
        adapter = new FiltersAdapter(this);
        filtersView.setAdapter(adapter);
        filtersView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent data = new Intent();
                data.putExtra("", 0/*TODO: здесь передаём фильтр в предыдущую активность*/);
                setResult(RESULT_OK, data);
                finish();
                return false;
            }
        });
    }
}