package com.example.gymtracker.Model;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    private float weightLifted;
    private int maximumReps;
    private String title;


    public Exercise()
    {
        //Default Constructor
    }

    public Exercise(float weightLifted, int maximumReps)
    {
        this.setWeightLifted(weightLifted);
        this.setMaximumReps(maximumReps);
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


    //probably wrong
    public String getTitle() {
        return title;
    }

}
