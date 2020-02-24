package com.example.gymtracker.Model;

public class Measurement {

    private float weight;

    //Default Constructor
    public Measurement()
    {
    }

    //Main Constructor
    public Measurement(float weight)
    {
        this.setWeight(weight);
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
