package com.example.gymtracker.Model;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    private float weightLifted;
    private int maximumReps;
    private List<String> exerciseTitles = new ArrayList<>();

    //Default Constructor
    public Exercise() //Default Constructor
    {
        exerciseTitles.add("Bench Press");
        exerciseTitles.add("Leg Press");
    }

    public Exercise(String exerciseTitles, float weightLifted, int maximumReps)
    {
        this.setWeightLifted(weightLifted);
        this.setMaximumReps(maximumReps);



        this.exerciseTitles.add("Bench Press");
        this.exerciseTitles.add("Leg Press");
    }

    public float getWeightLifted() {
        return weightLifted;
    }

    public void setWeightLifted(float weightLifted) {
        this.weightLifted = weightLifted;
    }

    public int getMaximumReps() {
        return maximumReps;
    }

    public void setMaximumReps(int maximumReps) {
        this.maximumReps = maximumReps;
    }

    public List<String> getExerciseTitles() {
        return exerciseTitles;
    }

}
