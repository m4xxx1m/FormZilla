package ru.raptors.team.formzilla.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.adapters.FiltersExpandableListAdapter;
import ru.raptors.team.formzilla.models.Filter;
import ru.raptors.team.formzilla.models.User;

public class ChoosingFiltersActivity extends AppCompatActivity {

    private ExpandableListView filtersView;
    private FiltersExpandableListAdapter adapter;

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
        User nowUser = User.getNowUser(this);
        adapter = new FiltersExpandableListAdapter(this, nowUser.getAllCategories(), nowUser.getFiltersAsHashMap());
        filtersView.setAdapter(adapter);
        filtersView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent data = new Intent();
                String category = adapter.getGroup(groupPosition).toString();
                String filter = adapter.getChild(groupPosition, childPosition).toString();
                data.putExtra(CreatingFormActivity.CHOSEN_FILTER, nowUser.findFilterByFilterAndCategory(filter, category));
                setResult(RESULT_OK, data);
                finish();
                return false;
            }
        });
    }
}