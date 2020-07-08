package com.example.gymtracker.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gymtracker.Model.Exercise;
import com.example.gymtracker.Model.ExerciseData;
import com.example.gymtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ExercisePageFragment extends Fragment implements View.OnClickListener {

    private TextView exerciseName;
    private EditText weightLiftedInput;
    private EditText repsInput;
    private Button weightAndRepsBtn;
    private TextView weightAndRepsView;

    private Exercise exercise;
    private ExerciseData exerciseData;
    private static final String TAG = "YOUR-TAG-NAME";
    private String title;
    private String KEY_TITLE = "title";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        final String itemClickedId = getArguments().getString("itemClickedId");

        exercise = new Exercise();
        exerciseData = new ExerciseData();

        weightAndRepsBtn.setOnClickListener(this);

        //Read data from Cloud Firestore
        db.collection("exercise_list")
                .document(itemClickedId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                String exerciseTitle = document.getString(KEY_TITLE);
                                String exerciseId = document.getId();

                                exerciseData.setTitle(exerciseTitle);
                                exerciseData.setExerciseId(exerciseId);

                                exerciseName.setText(exerciseData.getTitle());

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

//        exerciseName.setText(exerciseData.getTitle());


        //Read data from Firebase Realtime Database
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

        //Write data to Firebase Realtime Database
        weightLiftedRef.setValue(exercise.getWeightLifted());
        maximumRepsRef.setValue(exercise.getMaximumReps());

        //Write data to Firebase Cloud Firestore
    }

}
