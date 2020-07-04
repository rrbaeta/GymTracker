package com.example.gymtracker.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gymtracker.Model.ExerciseData;
import com.example.gymtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ExerciseFragment extends Fragment {

    private ListView exercisesListView;

    private static final String TAG = "YOUR-TAG-NAME";
    private String KEY_TITLE = "title";
    private String title;

    private ArrayAdapter<ExerciseData> exerciseDataArrayAdapter;
    private ArrayList<ExerciseData> exerciseDataArrayList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        exercisesListView = rootView.findViewById(R.id.exercisesListView);

        final ExerciseData exerciseData = new ExerciseData(title);

        if(exerciseDataArrayList != null)
        {
            exerciseDataArrayList.clear();
        }

        //Cloud Firestore exercise list to array adapter
        db.collection("exercise_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                ExerciseData exerciseData = document.toObject(ExerciseData.class);
                                exerciseData.setExerciseId(document.getId());

                                exerciseDataArrayList.add(exerciseData);

                                exerciseDataArrayAdapter = new ArrayAdapter(getContext(), R.layout.row, R.id.list_content, exerciseDataArrayList);
                                exercisesListView.setAdapter(exerciseDataArrayAdapter);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();

                //get exercise clicked adapter (it gets the ID from the document of respective item clicked from Cloud Firestore)
                ExerciseData exerciseData1 = (ExerciseData) exercisesListView.getAdapter().getItem(position);
                String itemClickedId = exerciseData1.getExerciseId();

                String itemClicked = exerciseData1.getTitle();
                Toast.makeText(getContext(),itemClicked + "", Toast.LENGTH_SHORT).show();

                //TODO need to fix the data sent to the ExercisePageFragment, (probably need to send the all ExerciseData Class)
                bundle.putString("itemClicked", itemClicked);
                Navigation.findNavController(rootView).navigate(R.id.action_nav_exercise_to_exercisePageFragment, bundle);

            }
        });

        return rootView;
    }
}

