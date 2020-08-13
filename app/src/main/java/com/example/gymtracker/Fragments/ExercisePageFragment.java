package com.example.gymtracker.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gymtracker.Model.Exercise;
import com.example.gymtracker.Model.ExerciseData;
import com.example.gymtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class ExercisePageFragment extends Fragment implements View.OnClickListener {

    private TextView exerciseName;
    private EditText weightLiftedInput;
    private EditText repsInput;
    private Button weightAndRepsBtn;
    private TextView weightAndRepsView;

    private Exercise exercise;
    private ExerciseData exerciseData;
    private FirebaseAuth mAuth;

    private static final String TAG = "YOUR-TAG-NAME";
    private String title;
    private String uid;
    private String KEY_TITLE = "title";
    private String KEY_UID = "uid";
    private String KEY_ExerciseID = "exerciseId";
    private String KEY_WeightLifted = "weightLifted";
    private String KEY_MaximumReps = "maximumReps";
    private String exerciseId;
    private String userExerciseDataDocId = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ExercisePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercise_page, container, false);

        mAuth = FirebaseAuth.getInstance();

        exerciseName = rootView.findViewById(R.id.exerciseName);
        weightLiftedInput = rootView.findViewById(R.id.weightLiftedInput);
        repsInput = rootView.findViewById(R.id.repsInput);
        weightAndRepsBtn = rootView.findViewById(R.id.weightAndRepsBtn);
        weightAndRepsView = rootView.findViewById(R.id.weightandRepsView);

        exerciseId = getArguments().getString("itemClickedId");

        exercise = new Exercise();
        exerciseData = new ExerciseData();

        //TODO onStart() probably shouldn't be called here
        onStart();

        weightAndRepsBtn.setOnClickListener(this);

        //Read exercise data from Cloud Firestore
        db.collection("exercise_list")
                .document(exerciseId)
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


        //Read the user data for the exercise from Cloud Firestore
        db.collection("userExerciseData")
                .whereEqualTo(KEY_UID, uid)
                .whereEqualTo(KEY_ExerciseID, exerciseId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                userExerciseDataDocId = document.getId();

                                float weightLifted = document.getDouble(KEY_WeightLifted).floatValue();
                                int maximumReps = document.getLong(KEY_MaximumReps).intValue();

                                exercise.setWeightLifted(weightLifted);
                                exercise.setMaximumReps(maximumReps);

                                weightAndRepsView.setText("Weight Lifted: " + String.valueOf(exercise.getWeightLifted()) + "\nMaximum Reps: " + String.valueOf(exercise.getMaximumReps()));

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
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


        //Write data to Firebase Cloud Firestore
        //Check if the user has set the data before to Cloud Firestore
        db.collection("userExerciseData")
                .whereEqualTo(KEY_UID, uid)
                .whereEqualTo(KEY_ExerciseID, exerciseId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                userExerciseDataDocId = document.getId();

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // Update/create the user document
        if (userExerciseDataDocId == null)
        {

            //Write weightLifted and reps value to Cloud Firestore
            Map<String, Object> userExerciseData = new HashMap<>();
            userExerciseData.put(KEY_UID, uid);
            userExerciseData.put(KEY_ExerciseID, exerciseId);
            userExerciseData.put(KEY_WeightLifted, Double.valueOf(exercise.getWeightLifted()));
            userExerciseData.put(KEY_MaximumReps, exercise.getMaximumReps());

            db.collection("userExerciseData")
                    .add(userExerciseData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

        }
        else if (userExerciseDataDocId != null)
        {

            //Write weightLifted and reps value to Cloud Firestore
            Map<String, Object> userExerciseData = new HashMap<>();
            userExerciseData.put(KEY_WeightLifted, Double.valueOf(exercise.getWeightLifted()));
            userExerciseData.put(KEY_MaximumReps, exercise.getMaximumReps());

            db.collection("userExerciseData")
                    .document(userExerciseDataDocId)
                    .update(userExerciseData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user){
        if(user != null)
        {

            //get user ID
            uid = user.getUid();

        }
        else
        {
        }

    }

}
