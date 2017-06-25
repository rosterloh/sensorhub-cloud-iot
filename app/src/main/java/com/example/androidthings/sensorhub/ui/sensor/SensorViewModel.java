package com.example.androidthings.sensorhub.ui.sensor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.androidthings.sensorhub.sensors.SensorHub;
import com.example.androidthings.sensorhub.sensors.Sensors;
import com.example.androidthings.sensorhub.util.AppExecutors;

import javax.inject.Inject;

public class SensorViewModel extends ViewModel {

    private final SensorHub sensorHub;

    @Inject
    SensorViewModel(SensorHub sensorHub) {
        this.sensorHub = sensorHub;
    }

    LiveData<Sensors> getSensorData() {
        return sensorHub.sensorData;
    }
}
