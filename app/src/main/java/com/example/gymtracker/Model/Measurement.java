package com.example.gymtracker.Model;

public class Measurement {

    private float weight;

    //Constructor
    public Measurement(float weight)
    {
        this.setWeight(weight);
    }

    //Default Constructor
    public Measurement()
    {
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
