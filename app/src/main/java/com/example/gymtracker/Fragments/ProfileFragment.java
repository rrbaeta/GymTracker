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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gymtracker.Model.Measurement;
import com.example.gymtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment implements View.OnClickListener {


    private EditText weightInput;
    private Button weightBtn;
    private TextView weightOutput;
    private ImageView userAvatar;
    private TextView userName;

    private Measurement measurement;
    private FirebaseAuth mAuth;

    private static final String TAG = "YOUR-TAG-NAME";
    private String KEY_WEIGHT = "weight";
    private String KEY_UID = "uid";
    private String uid;
    private String userDocId = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        weightInput = rootView.findViewById(R.id.weightLiftedInput);
        weightBtn = rootView.findViewById(R.id.weightBtn);
        weightOutput = rootView.findViewById(R.id.weightOutput);
        userAvatar = rootView.findViewById(R.id.userAvatar);
        userName = rootView.findViewById(R.id.userName);

        weightBtn.setOnClickListener(this);

        measurement = new Measurement();


        //TODO onStart() probably shouldn't be called here
        onStart();

        //Read user data from Cloud Firestore
        db.collection("users")
                .whereEqualTo(KEY_UID, uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                userDocId = document.getId();

                                float weight = document.getDouble(KEY_WEIGHT).floatValue();

                                measurement.setWeight(weight);

                                weightOutput.setText("Weight: " + String.valueOf(measurement.getWeight()) + " Kg");

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


        //Check if the user has set the data before to Cloud Firestore
        db.collection("users")
                .whereEqualTo(KEY_UID, uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                userDocId = document.getId();

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        // Update/create the user document
        if (userDocId == null)
        {

            //Write weight value to Cloud Firestore
            Map<String, Object> user = new HashMap<>();
            user.put(KEY_WEIGHT, Double.valueOf(measurement.getWeight()));
            user.put(KEY_UID, uid);

            db.collection("users")
                    .add(user)
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
        else if (userDocId != null)
        {

            //Write weight value to Cloud Firestore
            Map<String, Object> user = new HashMap<>();
            user.put(KEY_WEIGHT, Double.valueOf(measurement.getWeight()));

            db.collection("users")
                    .document(userDocId)
                    .update(user)
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

            userName.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null)
            {
                String photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl + "?type=large";
                Picasso.get().load(photoUrl).into(userAvatar);
            }
        }
        else
        {
            userName.setText("You are not signed in");
        }

    }

}
