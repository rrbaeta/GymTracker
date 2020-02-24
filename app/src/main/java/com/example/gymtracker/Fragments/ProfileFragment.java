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

import com.example.gymtracker.Model.Measurement;
import com.example.gymtracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText weightInput;
    private Button weightBtn;
    private TextView weightOutput;

    private Measurement measurement;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("weight");

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

        //Read data from Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float weight = dataSnapshot.getValue(Float.class);

                measurement.setWeight(Float.valueOf(weight));

                weightOutput.setText(String.valueOf(measurement.getWeight()));
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
            case R.id.weightBtn:
                addWeight();
                break;
        }

    }

    private void addWeight()
    {
        measurement.setWeight(Float.valueOf(weightInput.getText().toString()));

        //Write weight value to Firebase
        myRef.setValue(measurement.getWeight());
    }

}
