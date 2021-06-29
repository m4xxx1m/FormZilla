package ru.raptors.team.formzilla.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.activities.EnterFormNameActivity;
import ru.raptors.team.formzilla.models.Form;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatedFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatedFormsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public final static String FORM = "form";

    // TODO: Rename and change types of parameters

    public CreatedFormsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CreatedFormsFragment newInstance() {
        CreatedFormsFragment fragment = new CreatedFormsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_created_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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