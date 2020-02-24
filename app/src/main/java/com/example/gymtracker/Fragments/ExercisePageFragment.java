package com.example.gymtracker.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gymtracker.Model.Exercise;
import com.example.gymtracker.R;


public class ExercisePageFragment extends Fragment {

    private TextView exerciseName;
    private EditText weightLiftedInput;
    private EditText repsInput;
    private Button weightAndRepsBtn;
    private TextView weightAndRepsView;

    private Exercise exercise;

    public ExercisePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercise_page, container, false);

        exerciseName = rootView.findViewById(R.id.exerciseName);
        weightLiftedInput = rootView.findViewById(R.id.weightLiftedInput);
        repsInput = rootView.findViewById(R.id.repsInput);
        weightAndRepsBtn = rootView.findViewById(R.id.weightAndRepsBtn);
        weightAndRepsView = rootView.findViewById(R.id.weightandRepsView);

        exercise = new Exercise();

        exerciseName.setText(getArguments().getString("itemClicked"));

        return rootView;
    }

}
