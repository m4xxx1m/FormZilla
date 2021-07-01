package ru.raptors.team.formzilla.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.adapters.CreatedFormsAdapter;
import ru.raptors.team.formzilla.adapters.PassedFormsAdapter;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassedFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassedFormsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout placeHolder;

    public PassedFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassesFormsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassedFormsFragment newInstance(String param1, String param2) {
        PassedFormsFragment fragment = new PassedFormsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passed_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User nowUser = User.getNowUser(getContext());
        if(nowUser != null) {
            ArrayList<Form> passedForms = nowUser.getPassedForms();
            PassedFormsAdapter passedFormsAdapter = new PassedFormsAdapter(getContext(), passedForms);
            RecyclerView passedFormsRecyclerView = view.findViewById(R.id.passedFormsRecyclerView);
            passedFormsRecyclerView.setAdapter(passedFormsAdapter);
            if (passedForms == null || passedForms.isEmpty()) {
                placeHolder = getActivity().findViewById(R.id.place_holder);
                placeHolder.removeView(view.findViewById(R.id.passedFormsRecyclerView));
                TextView textView = new TextView(getContext().getApplicationContext());
                textView.setText("Нет пройденных опросов");
                placeHolder.addView(textView);
            }
        }
    }
}