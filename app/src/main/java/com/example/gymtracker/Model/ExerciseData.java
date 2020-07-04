package com.example.gymtracker.Model;

import com.google.firebase.database.Exclude;

public class ExerciseData
{

    private String title;
    private String exerciseId;

    public ExerciseData()
    {}

    public ExerciseData(String title)
    {
        this.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Exclude
    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }


    @Override
    public String toString() {
        return title;
    }

}
