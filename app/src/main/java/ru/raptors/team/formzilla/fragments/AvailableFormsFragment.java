package ru.raptors.team.formzilla.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.adapters.AvailableFormAdapter;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailableFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailableFormsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ConstraintLayout placeHolder;

    public AvailableFormsFragment() {
        // Required empty public constructor
    }

    public static AvailableFormsFragment newInstance() {
        AvailableFormsFragment fragment = new AvailableFormsFragment();
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
        return inflater.inflate(R.layout.fragment_available_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User nowUser = User.getNowUser(getContext());
        if(nowUser != null) {
            ArrayList<Form> availableForms = nowUser.getAvailableForms();
            AvailableFormAdapter availableFormAdapter = new AvailableFormAdapter(getContext(), availableForms);
            RecyclerView availableFormsRecyclerView = view.findViewById(R.id.recycler_view);
            availableFormsRecyclerView.setAdapter(availableFormAdapter);
            availableFormsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            if (availableForms == null || availableForms.size() == 0) {
                placeHolder = getActivity().findViewById(R.id.place_holder);
                placeHolder.removeView(view.findViewById(R.id.recycler_view));
                TextView textView = new TextView(getContext().getApplicationContext());
                textView.setText("Нет доступных опросов");
                placeHolder.addView(textView);
            }
        }
    }
}