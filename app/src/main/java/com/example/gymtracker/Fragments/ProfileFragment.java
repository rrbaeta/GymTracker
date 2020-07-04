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

import com.example.gymtracker.Model.Measurement;
import com.example.gymtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText weightInput;
    private Button weightBtn;
    private TextView weightOutput;

    private Measurement measurement;

    private static final String TAG = "YOUR-TAG-NAME";
    private String KEY_WEIGHT = "weight";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        weightInput = rootView.findViewById(R.id.weightLiftedInput);
        weightBtn = rootView.findViewById(R.id.weightBtn);
        weightOutput = rootView.findViewById(R.id.weightOutput);

        weightBtn.setOnClickListener(this);

        measurement = new Measurement();

        //Read data from Cloud Firestore
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "user" + document.getData());

                                if(document.exists())
                                {

                                    float weight = document.getDouble(KEY_WEIGHT).floatValue();

                                    measurement.setWeight(weight);

                                    weightOutput.setText("Weight: " + String.valueOf(measurement.getWeight()) + " Kg");

                                }
                                else{
                                    //Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                }
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
            case R.id.weightBtn:
                addWeight();
                break;
        }

    }

    private void addWeight()
    {
        measurement.setWeight(Float.valueOf(weightInput.getText().toString()));

        //Write weight value to Cloud Firestore
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_WEIGHT, Double.valueOf(measurement.getWeight()));

        db.collection("users").document("user")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

}
