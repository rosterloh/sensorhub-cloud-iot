package com.example.androidthings.sensorhub.di;

import com.example.androidthings.sensorhub.SensorHubApp;

public class AppInjector {

    public static void init(SensorHubApp sensorHubApp) {
        DaggerAppComponent.builder()
                .application(sensorHubApp)
                .build()
                .inject(sensorHubApp);
    }
}
