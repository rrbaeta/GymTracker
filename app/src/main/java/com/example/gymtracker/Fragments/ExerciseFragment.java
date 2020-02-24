package com.example.gymtracker.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gymtracker.Model.Exercise;
import com.example.gymtracker.R;


public class ExerciseFragment extends Fragment {

    private Button testBtn;
    private ListView exercisesListView;

    //Needs to be checked
    private Exercise exercise;
    private ArrayAdapter<Exercise> exerciseArrayAdapter;

    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        testBtn = rootView.findViewById(R.id.testBtn);
        exercisesListView = rootView.findViewById(R.id.exercisesListView);

        exercise = new Exercise();

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_nav_exercise_to_exercisePageFragment);
            }
        });

        exerciseArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, exercise.getExerciseTitles());
        exercisesListView.setAdapter(exerciseArrayAdapter);

        exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent intent = new Intent(getContext(),  ExercisePageFragment.this);

                //int itemClicked2 = (int) exercisesListView.getAdapter().getItem(position);

                Bundle bundle = new Bundle();

                String itemClicked = exercisesListView.getAdapter().getItem(position).toString();

                bundle.putString("itemClicked", itemClicked);

                Navigation.findNavController(rootView).navigate(R.id.action_nav_exercise_to_exercisePageFragment, bundle);


                Toast.makeText(getContext(),itemClicked + "", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
