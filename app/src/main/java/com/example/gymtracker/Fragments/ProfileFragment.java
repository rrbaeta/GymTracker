package com.example.gymtracker.Fragments;


import android.os.Bundle;

import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.Model.Measurement;
import com.example.gymtracker.R;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Measurement measurement;

    private EditText weightInput;
    private Button weightBtn;
    private TextView weightOutput;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        weightInput = rootView.findViewById(R.id.weightInput);
        weightBtn = rootView.findViewById(R.id.weightBtn);
        weightOutput = rootView.findViewById(R.id.weightOutput);

        weightBtn.setOnClickListener(this);

        measurement = new Measurement();

        return rootView;
    }

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

        weightOutput.setText(String.valueOf(measurement.getWeight()));
    }

}
