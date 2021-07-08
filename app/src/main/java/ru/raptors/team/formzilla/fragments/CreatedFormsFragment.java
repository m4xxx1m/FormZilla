package ru.raptors.team.formzilla.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.activities.EnterFormNameActivity;
import ru.raptors.team.formzilla.activities.MainActivity;
import ru.raptors.team.formzilla.adapters.CreatedFormsAdapter;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatedFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatedFormsFragment extends Fragment {

    public final static String FORM = "form";
    public final static String QUESTION = "question";

    private ConstraintLayout placeHolder;

    public CreatedFormsFragment() {
    }

    public static CreatedFormsFragment newInstance() {
        CreatedFormsFragment fragment = new CreatedFormsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_created_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User nowUser = User.getNowUser(getContext());
        if(nowUser != null) {
            ArrayList<Form> createdForms = nowUser.getCreatedForms();
            Log.i("UserForms", nowUser.formsToString());
            CreatedFormsAdapter createdFormsAdapter = new CreatedFormsAdapter(getContext(), createdForms, (MainActivity) getActivity());
            RecyclerView createdFormsRecyclerView = view.findViewById(R.id.created_forms_recycler_view);
            createdFormsRecyclerView.setAdapter(createdFormsAdapter);
            createdFormsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            if (createdForms == null || createdForms.isEmpty()) {
                placeHolder = getActivity().findViewById(R.id.place_holder);
                placeHolder.removeView(view.findViewById(R.id.created_forms_recycler_view));
                TextView textView = new TextView(getContext().getApplicationContext());
                textView.setText("Нет созданных опросов");
                placeHolder.addView(textView);
            }
        }
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.createFormButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Form form = new Form();
                Intent  createFormIntent = new Intent(getActivity(), EnterFormNameActivity.class);
                createFormIntent.putExtra(FORM, form);
                startActivity(createFormIntent);
            }
        });
    }
}