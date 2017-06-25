package com.example.androidthings.sensorhub.sensors;

public class Sensors {

    public long timestamp;
    public final Float temperature;
    public final Float pressure;

    public Sensors(long timestamp, Float temperature, Float pressure) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.pressure = pressure;
    }
}
