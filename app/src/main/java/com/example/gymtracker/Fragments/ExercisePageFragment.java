package com.example.gymtracker.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ExercisePageFragment extends Fragment implements View.OnClickListener {

    private TextView exerciseName;
    private EditText weightLiftedInput;
    private EditText repsInput;
    private Button weightAndRepsBtn;
    private TextView weightAndRepsView;

    private Exercise exercise;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference weightLiftedRef= database.getReference("weightLifted");
    DatabaseReference maximumRepsRef = database.getReference("maximumReps");

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

        weightAndRepsBtn.setOnClickListener(this);

        //Read data from Firebase
        weightLiftedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float weightLifted = dataSnapshot.getValue(Float.class);

                exercise.setWeightLifted(Float.valueOf(weightLifted));

                //Read data from Firebase
                maximumRepsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer maximumReps = dataSnapshot.getValue(Integer.class);

                        exercise.setMaximumReps(Integer.valueOf(maximumReps));

                        weightAndRepsView.setText("Weight Lifted: " + String.valueOf(exercise.getWeightLifted()) + "\nMaximum Reps: " + String.valueOf(exercise.getMaximumReps()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        //weightOutput.setText(String.valueOf(measurement.getWeight()));
                        //Failed to Read Value
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //weightOutput.setText(String.valueOf(measurement.getWeight()));
                //Failed to Read Value
            }
        });

        return rootView;
    }

    //all onClicks
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.weightAndRepsBtn:
                addWeightLiftedAndReps();
                weightAndRepsView.setText("Weight Lifted: " + String.valueOf(exercise.getWeightLifted()) + "\nMaximum Reps: " + String.valueOf(exercise.getMaximumReps()));
                break;
        }

    }

    private void addWeightLiftedAndReps()
    {
        exercise.setWeightLifted(Float.valueOf(weightLiftedInput.getText().toString()));
        exercise.setMaximumReps(Integer.valueOf(repsInput.getText().toString()));

        //Write weight value to Firebase
        weightLiftedRef.setValue(exercise.getWeightLifted());
        maximumRepsRef.setValue(exercise.getMaximumReps());
    }

}
