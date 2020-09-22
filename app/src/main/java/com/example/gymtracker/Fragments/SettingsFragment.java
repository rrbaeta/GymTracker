package com.example.gymtracker.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gymtracker.R;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button logOutButton;
    private TextView textView;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        logOutButton = rootView.findViewById(R.id.logOutButton);
        textView = rootView.findViewById(R.id.textView);

        logOutButton.setOnClickListener(this);

        return rootView;
    }

    //all Clicks
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.logOutButton:

                textView.setText("You logged out");
                FirebaseAuth.getInstance().signOut();

                break;
        }
    }
}