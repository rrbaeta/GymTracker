package com.example.gymtracker.Model;

public class Measurement {

    private float weight;

    public Measurement(float weight)
    {
        //this.weight = weight;
        this.setWeight(weight);
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
