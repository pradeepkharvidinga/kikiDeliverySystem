package com.everestengineering.vehicle;

public class Vehicle {
    static int maxSpeed;
    static int maxCarriableWeight;
    static float currentTime = 0.0F;
    float availableTime;
    boolean isAvailable;

    public  Vehicle(){}

    public Vehicle(float availableTime, boolean isAvailable) {
        this.availableTime = availableTime;
        this.isAvailable = isAvailable;
    }

    public static int getMaxSpeed() {
        return maxSpeed;
    }

    public static void setMaxSpeed(int maxSpeed) {
        Vehicle.maxSpeed = maxSpeed;
    }

    public static int getMaxCarriableWeight() {
        return maxCarriableWeight;
    }

    public static void setMaxCarriableWeight(int maxCarriableWeight) {
        Vehicle.maxCarriableWeight = maxCarriableWeight;
    }

    public static float getCurrentTime() {
        return currentTime;
    }

    public static void setCurrentTime(float currentTime) {
        Vehicle.currentTime = currentTime;
    }

    public float getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(float availableTime) {
        this.availableTime = availableTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
